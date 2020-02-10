package jp.co.massu_p.nfccardreader.views.mainview

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.databases.userAssignInfo.UserAssignDBAdapter
import jp.co.massu_p.nfccardreader.file.FileExporter
import jp.co.massu_p.nfccardreader.file.format.Charset
import jp.co.massu_p.nfccardreader.file.format.ExportType
import jp.co.massu_p.nfccardreader.file.format.LineFeed
import jp.co.massu_p.nfccardreader.fileimport.FileImportActivity
import jp.co.massu_p.nfccardreader.models.UserAssignDataModel
import jp.co.massu_p.nfccardreader.nfc.MyNfcManager
import jp.co.massu_p.nfccardreader.utils.Extensions.getTagId
import jp.co.massu_p.nfccardreader.views.scanconfirm.ConfirmViewFragment
import jp.co.massu_p.nfccardreader.views.setting.SettingActivity

/**
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
class MainActivity : AppCompatActivity() {

	private val TAG = "CardScan"
	private val STORAGE_PERMISSION_REQUEST_CODE = 1

	lateinit private var myNfcManager: MyNfcManager
	lateinit private var userAssignDB: UserAssignDBAdapter
	lateinit private var pref: SharedPreferences
	lateinit private var userAssignDataModel: UserAssignDataModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		userAssignDataModel = ViewModelProviders.of(this)[UserAssignDataModel::class.java]

		userAssignDB = UserAssignDBAdapter(applicationContext)
		pref = getSharedPreferences(getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)

		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(
				arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE
			)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			STORAGE_PERMISSION_REQUEST_CODE -> {
				if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
					// 不許可の場合。実装は後で。
				}
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	override fun onResume() {
		super.onResume()
		myNfcManager = MyNfcManager(this)
	}

	override fun onPause() {
		super.onPause()
		myNfcManager.onPause(this)
	}

	/**
	 * NFC読み込み時にコールされる。
	 * スキャン待機画面以外では動作させない。
	 */
	override fun onNewIntent(intent: Intent?) {
		when (findNavController(R.id.main_fragment).currentDestination?.id) {
			R.id.scanViewFragment -> {
				intent?.let {
					val tag = it.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag
					Log.i(TAG, "${tag} ID:${tag.getTagId()}]")

					var userAssignRecord = userAssignDataModel.getRecord(tag)
					val bundle = bundleOf(ConfirmViewFragment.RECORD_EXTRA to userAssignRecord)
					findNavController(R.id.main_fragment).navigate(R.id.toScanConfirm, bundle)
				}
			}
		}
		super.onNewIntent(intent)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.scan_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (null == item) return super.onOptionsItemSelected(item)
		val recordList = userAssignDB.getAllRecords()
		val charset = Charset.getCurrent(applicationContext)
		val linefeed = LineFeed.getCurrent(applicationContext)

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
