package jp.co.massu_p.nfccardreader.file.format

/**
 * 改行コードの定義
 */
class LineFeed() {

	private val linefeed_CR = "CR"
	private val linefeed_LF = "LF"
	private val linefeed_CRLF = "CRLF"

	lateinit var type: TYPE

	constructor(type: TYPE) : this() {
		this.type = type
	}

	constructor(name: String?) : this() {
		if (name.isNullOrEmpty()) {
			this.type = TYPE.CRLF
			return
		}
		when (name) {
			linefeed_CR -> {
				this.type = TYPE.CR
			}
			linefeed_LF -> {
				this.type = TYPE.LF
			}
			linefeed_CRLF -> {
				this.type = TYPE.CRLF
			}
			TYPE.CR.toString() -> {
				this.type = TYPE.CR
			}
			TYPE.LF.toString() -> {
				this.type = TYPE.LF
			}
			TYPE.CRLF.toString() -> {
				this.type = TYPE.CRLF
			}
			else -> {
				this.type = TYPE.CRLF
			}
		}
	}

	companion object {
		fun displayNames(): ArrayList<String> {
			val array = arrayListOf<String>()
			for (value in TYPE.values()) {
				array.add(value.toString())
			}
			return array
		}
	}

	enum class TYPE {
		CR, LF, CRLF;
	}

	/**
	 * 改行コードを取得
	 */
	fun getCode(): String {
		return when (type) {
			TYPE.CR -> {
				"\r"
			}
			TYPE.LF -> {
				"\n"
			}
			TYPE.CRLF -> {
				"\r\n"
			}
		}
	}

	/**
	 * Index番号を取得
	 */
	fun ordinal(): Int {
		return type.ordinal
	}
}