package jp.co.massu_p.nfccardreader.databases.employeeInfo

import android.database.Cursor
import android.os.Parcelable
import jp.co.massu_p.nfccardreader.databases.DbColumn
import jp.co.massu_p.nfccardreader.databases.DbRecord
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 社員情報レコードの定義
 *
 * ## 概要
 * 以下のカラムが存在している
 * - tagId NFCタグ（主キー）
 * - cardId カードに記載されている番号
 * - employeeId 社員番号
 * - employeeName 社員名
 *
 */
@Parcelize
class EmployeeRecord() : DbRecord(), Parcelable {

	constructor(cursor: Cursor) : this() {
		tagId = cursor.getString(cursor.getColumnIndex(COLUMN.TAG_ID.columnName))
		cardId = cursor.getString(cursor.getColumnIndex(COLUMN.CARD_ID.columnName))
		employeeId = cursor.getString(cursor.getColumnIndex(COLUMN.EMPLOYEE_ID.columnName))
		employeeName = cursor.getString(cursor.getColumnIndex(COLUMN.EMPLOYEE_NAME.columnName))
	}

	override fun initializer(): MutableList<DbColumn> {
		val columnList = mutableListOf<DbColumn>()
		columnList.add(DbColumn(COLUMN.TAG_ID.columnName, DbColumn.ValueType.TEXT, true, false, false))
		columnList.add(DbColumn(COLUMN.CARD_ID.columnName, DbColumn.ValueType.TEXT, false, false, false))
		columnList.add(DbColumn(COLUMN.EMPLOYEE_ID.columnName, DbColumn.ValueType.TEXT, false, false, false))
		columnList.add(DbColumn(COLUMN.EMPLOYEE_NAME.columnName, DbColumn.ValueType.TEXT, false, false, false))
		return columnList
	}

	override fun createNewInstance(cursor: Cursor): EmployeeRecord {
		return EmployeeRecord(cursor)
	}

	override fun getColumnSize(): Int {
		return columnMap.size
	}

	override fun getPrimaryKey(): String {
		return COLUMN.TAG_ID.columnName
	}

	override fun getPrimaryValue(): String {
		return tagId
	}

	enum class COLUMN(val columnName: String) {
		TAG_ID("tagId"), // カードのタグID
		CARD_ID("cardId"), // カード表面のID
		EMPLOYEE_ID("employeeId"), // 社員番号
		EMPLOYEE_NAME("employeeName") // 社員名
	}

	@IgnoredOnParcel
	var tagId: String = ""
		set(value) {
			field = value
			updateColumn(COLUMN.TAG_ID.columnName, field)
		}

	@IgnoredOnParcel
	var cardId: String = ""
		set(value) {
			field = value.toUpperCase(Locale.ROOT)
			updateColumn(COLUMN.CARD_ID.columnName, field)
		}

	@IgnoredOnParcel
	var employeeId: String = ""
		set(value) {
			field = value.toLowerCase(Locale.ROOT)
			updateColumn(COLUMN.EMPLOYEE_ID.columnName, field)
		}

	@IgnoredOnParcel
	var employeeName: String = ""
		set(value) {
			field = value
			updateColumn(COLUMN.EMPLOYEE_NAME.columnName, field)
		}

}