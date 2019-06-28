package jp.co.massu_p.nfccardreader.scanconfirm

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.databases.employeeInfo.EmployeeDBAdapter
import jp.co.massu_p.nfccardreader.databases.employeeInfo.EmployeeRecord
import jp.co.massu_p.nfccardreader.utils.Extensions.getTagId

/**
 * スキャン結果確認画面
 *
 * ## 概要
 *
 * スキャン結果から社員情報の確認、修正、保存を行うActivity。
 *
 * ## 機能
 *
 * ### 画面表示
 *
 * DBに一致するTagIdのレコードが存在していればその情報を表示する。
 *
 * 存在していなければ、TagIdとCardIdを同じものとして表示する。
 *
 * ### Activity起動時(onCreate)
 *
 * 渡されたタグ情報を用いてDB内の社員情報を検索する。
 *
 * DBに一致するTagIdのレコードが存在していればその情報をConfirmViewFragmentに渡す。
 *
 * 存在していなければTagIdとCardIdを同一とした新しいレコードを作成し、ConfirmViewFragmentに渡す。
 *
 * ### OKボタンタップ時
 *
 * ConfirmViewFragmentで入力されたレコードをDBに保存する。
 *
 * TagId以外が未入力でも許容する。
 *
 */
class ScanConfirmActivity : AppCompatActivity(), ConfirmViewFragment.OnFragmentInteractionListener {

	companion object {
		private const val TAG_EXTRA: String = "TAG_EXTRA"

		fun intent(context: Context, tag: Tag): Intent =
			Intent(context, ScanConfirmActivity::class.java).putExtra(
				TAG_EXTRA, tag
			)
	}

	private val TAG = "ScanConfirm"

	lateinit private var tag: Tag
	lateinit private var employeeDB: EmployeeDBAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_scan_confirm)

		employeeDB = EmployeeDBAdapter(applicationContext)
		tag = intent.getParcelableExtra<Tag>(TAG_EXTRA)

		var employeeRecord = EmployeeRecord()
		val recordList = employeeDB.getRecord(tag)
		if (recordList.isNullOrEmpty()) {
			employeeRecord.tagId = tag.getTagId()
			employeeRecord.cardId = tag.getTagId()
		} else {
			employeeRecord = recordList.get(0) as EmployeeRecord
		}

		if (savedInstanceState == null) {
			val transaction = supportFragmentManager.beginTransaction()
			transaction.add(R.id.view_container, ConfirmViewFragment.newInstance(employeeRecord))
			transaction.commit()
		}
	}

	override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
		super.onSaveInstanceState(outState, outPersistentState)
		if (null != outState) outState.putParcelable(TAG, tag)
	}

	override fun onConfirmOk(record: EmployeeRecord) {
		Log.i(TAG, "onConfirmOK:${record.tagId}, ${record.cardId}, ${record.employeeId}, ${record.employeeName}")
		if (employeeDB.isRecord(EmployeeRecord.COLUMN.TAG_ID.columnName, record.tagId)) {
			employeeDB.updateRecord(record)
			Log.i(TAG, "update record.")
		} else {
			employeeDB.addRecord(record)
			Log.i(TAG, "Add record.")
		}
		finish()
	}

}
