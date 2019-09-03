package jp.co.massu_p.nfccardreader.cardscan

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.databases.employeeInfo.EmployeeDBAdapter
import jp.co.massu_p.nfccardreader.file.FileExporter
import jp.co.massu_p.nfccardreader.file.format.Charset
import jp.co.massu_p.nfccardreader.file.format.ExportType
import jp.co.massu_p.nfccardreader.file.format.LineFeed
import jp.co.massu_p.nfccardreader.fileimport.FileImportActivity
import jp.co.massu_p.nfccardreader.scanconfirm.ScanConfirmActivity
import jp.co.massu_p.nfccardreader.setting.SettingActivity
import jp.co.massu_p.nfccardreader.utils.Extensions.getTagId


/**
 * カードスキャン画面
 *
 * ## 概要
 *
 * 端末のリーダーでRFIDをスキャンするActivity。
 *
 * このActivityの起動中にNFCを有効にしている。
 *
 * メニューからファイル形式を選択してデータベースのテーブルをファイルとして出力出来る。
 *
 * ### スキャン対象の規格
 * - Type-A
 * - Type-B
 * - Type-F
 * - Type-V
 *
 * ## 機能
 *
 * ### Activity起動時(onCreate)
 *
 * 1. データベースのエクスポートに必要な外部ストレージのアクセス許可を求める。
 *     1. ※メニューのタップ時に変更するかも
 *
 * ### Activity表示時(onResume)
 *
 * 1. NFC起動の設定
 *     1. 他アプリの起動制限（NFCのIntentに反応するアプリがインストールされている場合、起動させない）
 *     1. NFCのBroadcastを受け取るIntentFilterの設定
 *
 * ### NFCのBroadcast受信時(onNewIntent)
 *
 * 1. Intentからタグを取得する
 * 1. スキャン結果確認画面にタグを渡し起動する
 *
 * ### メニュータップ時
 *
 * 1. データベース内のデータをファイル出力する
 * 1. CSV(カンマ区切り), TSV(タブ区切り) が選べる
 *
 */
class CardScanActivity : AppCompatActivity(), ScanViewFragment.OnFragmentInteractionListener {

	override fun onFragmentInteraction(uri: Uri) {
		// 現状未使用
	}

	private val TAG = "CardScan"
	private val STORAGE_PERMISSION_REQUEST_CODE = 1
	private val nfcAdapter: NfcAdapter by lazy { NfcAdapter.getDefaultAdapter(this) }

	lateinit private var employeeDB: EmployeeDBAdapter
	lateinit private var pref: SharedPreferences

	/**
	 * 読み込むNFCタイプの定義
	 */
	private val NFC_TYPES = arrayOf(
		arrayOf(NfcA::class.java.name),
		arrayOf(NfcB::class.java.name),
		arrayOf(NfcF::class.java.name),
		arrayOf(NfcV::class.java.name)
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_card_scan)

		employeeDB = EmployeeDBAdapter(applicationContext)
		pref = getSharedPreferences(getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)

		if (savedInstanceState == null) {
			val transaction = supportFragmentManager.beginTransaction()
			transaction.add(R.id.view_container, ScanViewFragment.newInstance())
			transaction.commit()
		}

		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(
				arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE
			)
		}

	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			STORAGE_PERMISSION_REQUEST_CODE -> {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 許可の場合。実装は後で。
				} else {
					// 不許可の場合。実装は後で。
				}
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	override fun onResume() {
		super.onResume()

		// 他のアプリを開かせないようにする対応
		val intent = Intent(applicationContext, this::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
		val nfcPendingIntent =
			PendingIntent.getActivity(applicationContext, 0, intent, 0)

		// NFC読み取りのためのIntentFilter
		val techDetectedFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
		val ndefDetectedFilter =
			IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("*/*") } // この指定な無くても大丈夫
		val urlDetectedFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED) // この指定は無くても大丈夫
		urlDetectedFilter.addDataScheme("http")
		urlDetectedFilter.addDataScheme("https")

		// アプリ起動中のみ読み込みを許可する
		nfcAdapter.enableForegroundDispatch(
			this,
			nfcPendingIntent,
			arrayOf(techDetectedFilter, ndefDetectedFilter, urlDetectedFilter),
			NFC_TYPES
		)
	}

	override fun onPause() {
		if (this.isFinishing) {
			nfcAdapter.disableForegroundDispatch(this)
		}
		super.onPause()
	}

	override fun onNewIntent(intent: Intent?) {
		intent?.let {
			val tag = it.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag
			Log.i(TAG, "Tag:${tag.getTagId()}")
			startActivity(ScanConfirmActivity.intent(this, tag))
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.scan_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (null == item) return super.onOptionsItemSelected(item)
		val recordList = employeeDB.getAllRecords()
		val charset = Charset(pref.getString(getString(R.string.pref_key_charset), ""))
		val linefeed = LineFeed(pref.getString(getString(R.string.pref_key_linefeed), ""))

		// 外部ストレージのアクセス許可がない場合、リクエストを送りたい
		when (item.itemId) {
			R.id.menu_export_csv -> {
				if (recordList.isEmpty()) {
					Toast.makeText(applicationContext, R.string.message_no_record_to_output, Toast.LENGTH_SHORT).show()
					return true
				}
				if (FileExporter().exportFile(recordList, ExportType(ExportType.TYPE.CSV), charset, linefeed)) {
					Toast.makeText(applicationContext, R.string.message_file_export_success, Toast.LENGTH_SHORT).show()
				} else {
					Toast.makeText(applicationContext, R.string.message_file_export_failed, Toast.LENGTH_SHORT).show()
				}
				return true
			}

			R.id.menu_export_tsv -> {
				if (recordList.isEmpty()) {
					Toast.makeText(applicationContext, R.string.message_no_record_to_output, Toast.LENGTH_SHORT).show()
					return true
				}
				if (FileExporter().exportFile(recordList, ExportType(ExportType.TYPE.TSV), charset, linefeed)) {
					Toast.makeText(applicationContext, R.string.message_file_export_success, Toast.LENGTH_SHORT).show()
				} else {
					Toast.makeText(applicationContext, R.string.message_file_export_failed, Toast.LENGTH_SHORT).show()
				}
				return true
			}

			R.id.menu_export_setting -> {
				startActivity(SettingActivity.intent(applicationContext))
				return true
			}

			R.id.menu_import_file -> {
				startActivity(FileImportActivity.intent(applicationContext))
				return true
			}

			else -> {
				//クラウドにUpすることも見据えてJSON形式にも対応したい
				return super.onOptionsItemSelected(item)
			}
		}
	}
}
