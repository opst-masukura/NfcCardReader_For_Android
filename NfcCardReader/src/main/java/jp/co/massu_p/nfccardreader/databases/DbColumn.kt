package jp.co.massu_p.nfccardreader.databases

import java.lang.StringBuilder

/**
 * データベースのカラム値の指定および制約
 *
 * ## 概要

 * SQLiteのデータ値をモデルにした。
 *
 * カラムのデータ型や制約を指定する。
 *
 * データ型の定義は ValueType としてクラス内で定義している。
 *
 * @param name カラム名
 * @param type データ型
 * @param isPrimaryKey プライマリーキーの指定
 * @param isNotNull NotNull制限の指定
 * @param isUnique Unique制限の指定
 * @exception IllegalArgumentException typeにNULL型を指定し、かつ、NOT NULL制約をtrueにした場合に発生する。
 *
 */
class DbColumn(
	val name: String,
	val type : ValueType,
	val isPrimaryKey: Boolean,
	private val isNotNull: Boolean,
	private val isUnique: Boolean
) {

	/**
	 * データ型の定義
	 *
	 * TEXT 文字列
	 * INTEGER 整数
	 * REAL 少数
	 * BLOB バイナリデータ
	 * NULL NULL値
	 */
	enum class ValueType {
		TEXT,
		INTEGER,
		REAL,
		BLOB,
		NULL
	}

	/**
	 * カラム値
	 *
	 * この値はNullable。
	 *
	 * ## 制約について
	 *
	 * NotNullを有効にしている場合、ValueTypeに従って初期化される。
	 * - ValueType.TEXT : ""
	 * - ValueType.INTEGER : 0
	 * - ValueType.REAL : 0
	 * - ValueType.BLOB : 空のByteArray
	 *
	 * その他の制約についてはこのクラスでは確認しない。
	 *
	 * @exception IllegalArgumentException NotNullを有効にしている場合にnullを指定すると発生。
	 *
	 */
	var value: Any? = null
		set(value) {
			if (isNotNull && value == null) throw IllegalArgumentException("NOT NULL constraint failed: $name")
			when (value) {
				is String -> {
					if (type == ValueType.TEXT) {
						field = value
					} else {
						throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to TEXT")
					}
				}
				is Byte -> {
					field = when (type) {
						ValueType.INTEGER -> value as Long
						ValueType.TEXT -> value.toString()
						else -> throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to INTEGER")
					}
				}
				is Short -> {
					field = when (type) {
						ValueType.INTEGER -> value as Long
						ValueType.TEXT -> value.toString()
						else -> throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to INTEGER")
					}
				}
				is Int -> {
					field = when (type) {
						ValueType.INTEGER -> value as Long
						ValueType.TEXT -> value.toString()
						else -> throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to INTEGER")
					}
				}
				is Long -> {
					field = when (type) {
						ValueType.INTEGER -> value
						ValueType.TEXT -> value.toString()
						else -> throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to INTEGER")
					}
				}
				is Float -> {
					field = when (type) {
						ValueType.REAL -> value as Double
						ValueType.TEXT -> value.toString()
						else -> throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to REAL")
					}
				}
				is Double -> {
					field = when (type) {
						ValueType.REAL -> value
						ValueType.TEXT -> value.toString()
						else -> throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to REAL")
					}
				}
				is ByteArray -> {
					if (type == ValueType.BLOB) {
						field = value
					} else {
						throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName} to BLOB")
					}
				}
				else -> {
					if (value == null) {
						field = null
					} else {
						throw IllegalArgumentException("Mismatch type ${value::class.java.simpleName}")
					}
				}
			}
		}

	init {
		when (type) {
			ValueType.TEXT -> {
				if (isNotNull) value = ""
			}
			ValueType.INTEGER -> {
				if (isNotNull) value = 0 as Long
			}
			ValueType.REAL -> {
				if (isNotNull) value = 0 as Double
			}
			ValueType.BLOB -> {
				if (isNotNull) value = byteArrayOf()
			}
			ValueType.NULL -> {
				if (isNotNull) throw IllegalArgumentException("Mismatch ValueType and Constraint")
			}
		}
	}

	/**
	 * create tableのSQL構文で使用するカラムの定義を生成する
	 */
	fun getCreateTableSql(): String {
		val sqlBuilder = StringBuilder()
		sqlBuilder.append(name)
		when (type) {
			ValueType.TEXT -> {
				sqlBuilder.append(" text")
			}
			ValueType.INTEGER -> {
				sqlBuilder.append(" integer")
			}
			ValueType.REAL -> {
				sqlBuilder.append(" real")
			}
			ValueType.BLOB -> {
				sqlBuilder.append(" blob")
			}
		}
		if (isPrimaryKey) sqlBuilder.append(" primary key")
		if (isNotNull) sqlBuilder.append(" not null")
		if (isUnique) sqlBuilder.append(" unique")
		return sqlBuilder.toString()
	}
}