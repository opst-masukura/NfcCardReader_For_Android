package jp.co.massu_p.nfccardreader.databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

/**
 * データベースの操作を行うクラス
 *
 * @param context データベース生成に必要なコンテキスト
 * @param table オープンしたいテーブルクラス
 *
 * ## 概要
 *
 * データベースを操作するクラス。
 *
 * データベースを使用するにはデータベースの定義としてDbTable, DbRecord, DbColumnを実装すること。
 *
 */
abstract class DbAdapter(context: Context, private val table: DbTable) {

	protected val db: SQLiteDatabase
	private val dbHelper: DbOpenHelper = DbOpenHelper(context, table)

	init {
		db = dbHelper.writableDatabase
	}

	/**
	 * 指定した値がDB内に存在しているか確認する
	 *
	 * @param key カラム名
	 * @param value カラム値
	 */
	fun isRecord(key: String, value: String): Boolean {
		return isRecord(key, arrayOf(value))
	}

	/**
	 * 指定した値がDB内に存在しているか確認する
	 *
	 * @param key カラム名
	 * @param values カラム値のリスト
	 */
	fun isRecord(key: String, values: Array<String>): Boolean {
		val selectSql = "select count(*) as cnt from ${table.name} where $key = ? "
		val cursor: Cursor = db.rawQuery(selectSql, values)
		cursor.moveToFirst()
		val count = cursor.getInt(cursor.getColumnIndex("cnt"))
		cursor.close()
		return 0 < count
	}

	/**
	 * 指定したレコードをDBに追加する
	 *
	 * @param record 追加したいレコード
	 */
	fun addRecord(record: DbRecord) {
		if (!record.hasNext()) {
			Log.w(this::class.java.simpleName, "addRecord:更新する値が無かった")
			return
		}

		val values = ContentValues()
		while (record.hasNext()) {
			val column = record.getColumn()
			when(column.type) {
				DbColumn.ValueType.INTEGER -> {
					values.put(column.name, column.value as Long)
				}
				DbColumn.ValueType.REAL -> {
					values.put(column.name, column.value as Double)
				}
				DbColumn.ValueType.TEXT -> {
					values.put(column.name, column.value as String)
				}
				DbColumn.ValueType.BLOB -> {
					values.put(column.name, column.value as ByteArray)
				}
			}
		}
		db.insertOrThrow(table.name, null, values)
	}

	/**
	 * 指定したレコードを更新する
	 *
	 * @param record 更新するレコード
	 */
	fun updateRecord(record: DbRecord) {
		if (!record.hasNext()) {
			Log.w(this::class.java.simpleName, "updateRecord:更新する値が無かった")
			return
		}

		val values = ContentValues()
		while (record.hasNext()) {
			val column = record.getColumn()
			if (column.isPrimaryKey) continue
			when(column.type) {
				DbColumn.ValueType.INTEGER -> {
					values.put(column.name, column.value as Long)
				}
				DbColumn.ValueType.REAL -> {
					values.put(column.name, column.value as Double)
				}
				DbColumn.ValueType.TEXT -> {
					values.put(column.name, column.value as String)
				}
				DbColumn.ValueType.BLOB -> {
					values.put(column.name, column.value as ByteArray)
				}
			}
		}
		if (values.size() > 0) {
			db.update(table.name, values, "${record.getPrimaryKey()}=?", arrayOf(record.getPrimaryValue()))
		} else {
			Log.w(this::class.java.simpleName, "updateRecord:更新する値が無かった")
		}
	}

	/**
	 * DB内のレコードを全て取得する
	 *
	 * @return 取得したレコード一覧
	 */
	fun getAllRecords(): List<DbRecord> {
		val selectSql = "select * from ${table.name} "
		val cursor: Cursor = db.rawQuery(selectSql, null)
		val recordList = table.createRecords(cursor)
		cursor.close()
		return recordList
	}
}