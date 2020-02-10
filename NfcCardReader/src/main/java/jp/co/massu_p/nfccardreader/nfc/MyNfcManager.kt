package jp.co.massu_p.nfccardreader.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV

class MyNfcManager(activity: Activity) {

	private val nfcAdapter: NfcAdapter

	/**
	 * 読み込むNFCタイプの定義
	 */
	private val NFC_TYPES = arrayOf(
		arrayOf(NfcA::class.java.name),
		arrayOf(NfcB::class.java.name),
		arrayOf(NfcF::class.java.name),
		arrayOf(NfcV::class.java.name)
	)

	init {
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity.applicationContext)

		// 他のアプリを開かせないようにする対応
		val intent = Intent(activity.applicationContext, activity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
		val nfcPendingIntent =
			PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

		// NFC読み取りのためのIntentFilter
		val techDetectedFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
		val ndefDetectedFilter =
			IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("*/*") } // この指定な無くても大丈夫
		val urlDetectedFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED) // この指定は無くても大丈夫
		urlDetectedFilter.addDataScheme("http")
		urlDetectedFilter.addDataScheme("https")

		// アプリ起動中のみ読み込みを許可する
		nfcAdapter.enableForegroundDispatch(
			activity,
			nfcPendingIntent,
			arrayOf(techDetectedFilter, ndefDetectedFilter, urlDetectedFilter),
			NFC_TYPES
		)
	}

	fun onPause(activity: Activity) {
		if (activity.isFinishing) {
			nfcAdapter.disableForegroundDispatch(activity)
		}
	}
}