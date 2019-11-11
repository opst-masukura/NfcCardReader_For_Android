package jp.co.massu_p.nfccardreader.file

import android.os.Environment
import android.util.Log
import jp.co.massu_p.nfccardreader.databases.DbRecord
import jp.co.massu_p.nfccardreader.file.format.Charset
import jp.co.massu_p.nfccardreader.file.format.ExportType
import jp.co.massu_p.nfccardreader.file.format.LineFeed
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.lang.StringBuilder

/**
 * レコードの一覧をファイルに出力するクラス
 *
 * ## 概要
 *
 * レコード内のデータ一覧を外部ストレージの/opstフォルダに出力する。
 *
 * ファイルの形式として、CSV, TSVの指定が出来る。
 *
 * データ型が文字列か数であればその値を出力する。
 *
 * データ型がバイナリデータであれば、カラムがバイナリデータだと表示する。
 *
 * ## 使用方法
 *
 * 1. インスタンスを生成する。
 * 1. exportFileを実行することでファイルを出力する。
 *     1. ファイルの文字コードのデフォルトはUTF-8。
 * 1. 出力先は外部ストレージ/opstに出力される。
 *
 * ## 指定可能な形式
 * - CSV
 * - TSV
 *
 * 指定したい：JSON
 *
 * ## 出力ファイル名
 * - CSV -> list_csv.txt
 * - TSV -> list_tsv.txt
 *
 *  ## 出力パターン
 *  - CSVかTSVを指定した場合、DbRecordの生成時に登録した順番でカラムの値を出力していく。
 *  - 次レコードは改行される。
 *
 *  ### 出力例
 *
 *  record1_value1,record1_value2,record1_value3
 *
 *  record2_value1,record2_value2,record2_value3
 *
 *  record3_value1,record3_value2,record3_value3
 *
 */
class FileExporter {

	private val exportFolderPath = Environment.getExternalStorageDirectory().path + "/opst"

	init {
		val exportFolder = File(exportFolderPath)
		if (!exportFolder.exists()) {
			exportFolder.mkdir()
		}
	}

	/**
	 * ファイルを出力する
	 *
	 * 文字コードの省略可能。その際の指定はUTF-8となる。
	 *
	 * DcRecordの生成時に登録した順番でカラムの値を出力していく。
	 *
	 * @param recordList 出力するレコードリスト
	 * @param exportType 出力するファイルの形式
	 * @param charset 文字コード
	 *
	 */
	fun exportFile(
		recordList: List<DbRecord>,
		exportType: ExportType,
		charset: Charset = Charset.default,
		lineFeed: LineFeed = LineFeed.default,
		fileName: String = "list"
	): Boolean {
		val textBuilder = StringBuilder()
		val file = File(exportFolderPath, fileName + exportType.getExtension())
		if (file.exists()) {
			file.delete()
		}
		for (record in recordList) {
			while (record.hasNext()) {
				val column = record.getColumn()
				when (column.value) {
					is Int -> {
						textBuilder.append(column.value as Int)
					}
					is Long -> {
						textBuilder.append(column.value as Long)
					}
					is Float -> {
						textBuilder.append(column.value as Float)
					}
					is Double -> {
						textBuilder.append(column.value as Double)
					}
					is String -> {
						textBuilder.append(column.value as String)
					}
					is ByteArray -> {
						textBuilder.append("${column.name} is BinaryData.")
					}
				}
				if (record.hasNext()) {
					textBuilder.append(exportType.getSeparator())
				} else {
					textBuilder.append(lineFeed.code)
				}
			}
		}

		try {
			FileOutputStream(file).use { fileOutputStream ->
				OutputStreamWriter(fileOutputStream, charset.code).use { outputStreamWriter ->
					BufferedWriter(outputStreamWriter).use { bw ->
						bw.write(textBuilder.toString())
						bw.flush()
					}
				}
			}
		} catch (e: Exception) {
			Log.w(this::class.java.simpleName, "Failed exportFile ${e.message}")
			return false
		}
		return true
	}
}