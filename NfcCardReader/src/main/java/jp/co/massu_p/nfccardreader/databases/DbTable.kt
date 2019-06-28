package jp.co.massu_p.nfccardreader.databases

import android.database.Cursor
import android.util.Log
import java.lang.StringBuilder

/**
 * データベースのテーブルの定義
 *
 * @param name テーブル名
 * @param version バージョン番号
 * @param record 実装したレコード
 */
abstract class DbTable (val name: String, val version: Int, val record: DbRecord) {

	/**
	 * create tableのSQL構文を生成する
	 */
	fun getCreateTableSql(): String {
		Log.i(DbTable::class.java.simpleName, "create table -> $name");
		val sqlBuilder = StringBuilder()
		sqlBuilder.append("create table $name (${record.getCreateTableSql()});")
		return sqlBuilder.toString()
	}

	/**
	 * クエリの結果から新しいレコード一覧を生成する
	 *
	 * @param cursor クエリの結果
	 * @return レコード一覧
	 */
	fun createRecords(cursor: Cursor): List<DbRecord> {
		val records = arrayListOf<DbRecord>()
		var isNext = cursor.moveToFirst()
		while(isNext) {
			records.add(record.createNewInstance(cursor))
			isNext = cursor.moveToNext()
		}
		return records
	}
}