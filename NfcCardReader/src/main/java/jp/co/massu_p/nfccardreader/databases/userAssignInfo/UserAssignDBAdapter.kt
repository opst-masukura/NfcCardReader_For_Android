package jp.co.massu_p.nfccardreader.databases.userAssignInfo

import android.content.Context
import android.database.Cursor
import android.nfc.Tag
import jp.co.massu_p.nfccardreader.databases.DbAdapter
import jp.co.massu_p.nfccardreader.databases.DbRecord
import jp.co.massu_p.nfccardreader.utils.Extensions.getTagId

/**
 * ユーザー割り当て情報テーブルを操作するクラス
 */
class UserAssignDBAdapter(
	context: Context,
	private val userAssignTable: UserAssignTable = UserAssignTable(
		UserAssignTable.DB_NAME,
		UserAssignTable.VERSION,
		UserAssignRecord()
	)
) : DbAdapter(context, userAssignTable) {

	/**
	 * Tagの情報がDBに存在しているか確認する
	 *
	 * @param tag 確認するタグ
	 */
	fun isRecord(tag: Tag): Boolean {
		return isRecord(UserAssignRecord.COLUMN.TAG_ID.columnName, tag.getTagId())
	}

	/**
	 * DBからTagの情報を取得する
	 *
	 * @param tag 取得するタグ情報
	 */
	fun getRecord(tag: Tag): List<DbRecord> {
		val selectSql = "select * from ${userAssignTable.name} where tagId = ? "
		val cursor: Cursor = db.rawQuery(selectSql, arrayOf(tag.getTagId()))
		val recordList = userAssignTable.createRecords(cursor)
		cursor.close()
		return recordList
	}

}