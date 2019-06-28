package jp.co.massu_p.nfccardreader.databases.employeeInfo

import android.content.Context
import android.database.Cursor
import android.nfc.Tag
import jp.co.massu_p.nfccardreader.databases.DbAdapter
import jp.co.massu_p.nfccardreader.databases.DbRecord
import jp.co.massu_p.nfccardreader.utils.Extensions.getTagId

/**
 * 社員情報テーブルを操作するクラス
 */
class EmployeeDBAdapter(
	context: Context,
	private val employeeTable: EmployeeTable = EmployeeTable(
		EmployeeTable.DB_NAME,
		EmployeeTable.VERSION,
		EmployeeRecord()
	)
) : DbAdapter(context, employeeTable) {

	/**
	 * Tagの情報がDBに存在しているか確認する
	 *
	 * @param tag 確認するタグ
	 */
	fun isRecord(tag: Tag): Boolean {
		return isRecord(EmployeeRecord.COLUMN.TAG_ID.columnName, tag.getTagId())
	}

	/**
	 * DBからTagの情報を取得する
	 *
	 * @param tag 取得するタグ情報
	 */
	fun getRecord(tag: Tag): List<DbRecord> {
		val selectSql = "select * from ${employeeTable.name} where tagId = ? "
		val cursor: Cursor = db.rawQuery(selectSql, arrayOf(tag.getTagId()))
		val recordList = employeeTable.createRecords(cursor)
		cursor.close()
		return recordList
	}

}