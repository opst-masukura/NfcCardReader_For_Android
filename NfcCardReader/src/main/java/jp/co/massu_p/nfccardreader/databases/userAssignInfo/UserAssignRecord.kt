package jp.co.massu_p.nfccardreader.databases.userAssignInfo

import android.database.Cursor
import android.os.Parcelable
import jp.co.massu_p.nfccardreader.databases.DbColumn
import jp.co.massu_p.nfccardreader.databases.DbRecord
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * ユーザー割り当て情報レコードの定義
 *
 * ## 概要
 * 以下のカラムが存在している
 * - tagId NFCタグ（主キー）
 * - cardId カードに記載されている番号
 * - employeeId ユーザー番号
 * - employeeName ユーザー名
 *
 */
@Parcelize
class UserAssignRecord() : DbRecord(), Parcelable {

	constructor(cursor: Cursor) : this() {
		tagId = cursor.getString(cursor.getColumnIndex(COLUMN.TAG_ID.columnName))
		cardId = cursor.getString(cursor.getColumnIndex(COLUMN.CARD_ID.columnName))
		userId = cursor.getString(cursor.getColumnIndex(COLUMN.USER_ID.columnName))
		userName = cursor.getString(cursor.getColumnIndex(COLUMN.USER_NAME.columnName))
	}

	override fun initializer(): MutableList<DbColumn> {
		val columnList = mutableListOf<DbColumn>()
		columnList.add(DbColumn(COLUMN.TAG_ID.columnName, DbColumn.ValueType.TEXT, true, false, false))
		columnList.add(DbColumn(COLUMN.CARD_ID.columnName, DbColumn.ValueType.TEXT, false, false, false))
		columnList.add(DbColumn(COLUMN.USER_ID.columnName, DbColumn.ValueType.TEXT, false, false, false))
		columnList.add(DbColumn(COLUMN.USER_NAME.columnName, DbColumn.ValueType.TEXT, false, false, false))
		return columnList
	}

	override fun createNewInstance(cursor: Cursor): UserAssignRecord {
		return UserAssignRecord(cursor)
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
		USER_ID("userId"), // ユーザー番号
		USER_NAME("userName") // ユーザー名
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
	var userId: String = ""
		set(value) {
			field = value.toLowerCase(Locale.ROOT)
			updateColumn(COLUMN.USER_ID.columnName, field)
		}

	@IgnoredOnParcel
	var userName: String = ""
		set(value) {
			field = value
			updateColumn(COLUMN.USER_NAME.columnName, field)
		}

}