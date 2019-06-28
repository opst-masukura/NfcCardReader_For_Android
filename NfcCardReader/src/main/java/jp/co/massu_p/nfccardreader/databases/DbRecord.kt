package jp.co.massu_p.nfccardreader.databases

import android.database.Cursor
import java.lang.StringBuilder

/**
 * データベースのテーブルに保持されるレコードの定義
 *
 * ## 使用方法
 *
 * 1. initializerでDbColumnのリストを作成しreturnする。
 * 1. リストに登録した順番でレコードのカラムになる。
 *
 */
abstract class DbRecord() {

	protected val columnMap = mutableMapOf<String, DbColumn>()
	private var iterator: MutableIterator<MutableMap.MutableEntry<String, DbColumn>>

	init {
		for (column in initializer()) {
			columnMap.put(column.name, column)
		}
		iterator = columnMap.iterator()
	}

	/**
	 * create tableのSQL構文で使用するレコードの定義を生成する
	 */
	fun getCreateTableSql(): String {
		val sqlBuilder = StringBuilder()
		while (iterator.hasNext()) {
			val column: DbColumn = iterator.next().value
			sqlBuilder.append(column.getCreateTableSql())
			if (iterator.hasNext()) {
				sqlBuilder.append(", ")
			}
		}
		return sqlBuilder.toString()
	}

	/**
	 * カラム値を更新する
	 *
	 * @param name 更新するカラム名
	 * @param value 更新する値
	 */
	fun updateColumn(name: String, value: Any) {
		if (null != columnMap[name]) {
			columnMap[name]!!.value = value
		}
	}

	/**
	 * カラムの定義を行う
	 *
	 * ## 実装方法
	 *
	 * 実装時にDbColumnのリストを作成し返却する
	 *
	 * @return レコードのカラムリスト
	 */
	abstract fun initializer(): MutableList<DbColumn>

	/**
	 * クエリの結果から新しいレコードを生成する
	 *
	 * ## 実装方法
	 *
	 * 1. クエリの結果から実装クラスで定義しているカラムの値を取得する。
	 * 1. 取得したカラム値から実装クラスを新しく作成して返却する。
	 *
	 * @param cursor クエリの結果
	 * @return 実装するレコードクラス
	 */
	abstract fun createNewInstance(cursor: Cursor): DbRecord

	/**
	 * レコード内のカラム数を返す
	 *
	 * @return カラム数
	 */
	abstract fun getColumnSize(): Int

	/**
	 * プライマリーキーの名前を返す
	 *
	 * @return プライマリーキーの名前
	 */
	abstract fun getPrimaryKey(): Any

	/**
	 * プライマリーキーの値を返す
	 */
	abstract fun getPrimaryValue(): String

	/**
	 * イテレーター関連
	 * レコード内のカラムに順番にアクセスする際に使用する
	 */

	/**
	 * 次のカラムの存在確認
	 *
	 * @return true:次カラムが存在している false:次カラムが無い
	 */
	fun hasNext(): Boolean {
		return iterator.hasNext()
	}

	/**
	 * カラムのアクセス位置を先頭に戻す
	 */
	fun moveToFirst() {
		iterator = columnMap.iterator()
	}

	/**
	 * カラムを取得し、次の位置へ移動する
	 *
	 * @return 取得したカラムクラス
	 */
	fun getColumn(): DbColumn {
		return iterator.next().value
	}
}