package jp.co.massu_p.nfccardreader.models

import android.app.Application
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import jp.co.massu_p.nfccardreader.databases.userAssignInfo.UserAssignDBAdapter
import jp.co.massu_p.nfccardreader.databases.userAssignInfo.UserAssignRecord
import jp.co.massu_p.nfccardreader.utils.Extensions.getTagId

/**
 * ユーザー割り当て情報を管理するViewModel
 */
class UserAssignDataModel(val context: Application) : AndroidViewModel(context) {

	private val TAG = "UserAssignDataModel"
	private val userAssignDB: UserAssignDBAdapter = UserAssignDBAdapter(context)

	/**
	 * TagIdに割り当てられているユーザー割り当て情報を取得する。
	 *
	 * @param tag NFC Tag情報
	 * @return ユーザー割り当て情報
	 */
	fun getRecord(tag: Tag) : UserAssignRecord {
		var employeeRecord = UserAssignRecord()
		val recordList = userAssignDB.getRecord(tag)
		if (recordList.isNullOrEmpty()) {
			employeeRecord.tagId = tag.getTagId()
			employeeRecord.cardId = tag.getTagId()
		} else {
			employeeRecord = recordList.get(0) as UserAssignRecord
		}
		return employeeRecord
	}

	/**
	 * ユーザー割り当て情報を更新する。
	 *
	 * @param record Tagに割り当てるユーザー割り当て情報
	 */
	fun setRecord(record: UserAssignRecord) {
		if (userAssignDB.isRecord(UserAssignRecord.COLUMN.TAG_ID.columnName, record.tagId)) {
			userAssignDB.updateRecord(record)
			Log.i(TAG, "update record.")
		} else {
			userAssignDB.addRecord(record)
			Log.i(TAG, "Add record.")
		}
	}
}