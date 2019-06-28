package jp.co.massu_p.nfccardreader.file.format

/**
 * ファイルの出力形式の定義
 */
class ExportType(private val type: TYPE) {

	enum class TYPE {
		CSV, TSV;
	}

	/**
	 * 拡張子を取得する
	 */
	fun getExtension(): String {
		return when(type) {
			TYPE.CSV -> ".csv"
			TYPE.TSV -> ".tsv"
		}
	}

	/**
	 * 区切り文字を取得する
	 */
	fun getSeparator(): String {
		return when(type) {
			TYPE.CSV -> ","
			TYPE.TSV -> "\t"
		}
	}
}