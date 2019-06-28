package jp.co.massu_p.nfccardreader.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * データベースをオープンする
 *
 * データベースを使用するにはデータベースの定義としてDbTable, DbRecord, DbColumnを実装すること。
 *
 * @param context データベース生成に必要なコンテキスト
 * @param table オープンしたいテーブルクラス
 *
 */
class DbOpenHelper(context: Context, private val table: DbTable) :
	SQLiteOpenHelper(context, table.name, null, table.version) {

	override fun onCreate(db: SQLiteDatabase?) {
		db?.execSQL(table.getCreateTableSql())
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}