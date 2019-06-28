package jp.co.massu_p.nfccardreader.file.format

/**
 * 文字コードの定義
 */
class Charset() {

	private val charset_UTF8 = "UTF-8"
	private val charset_SJIS = "windows-31j"

	private val dispName_UTF8 = "UTF-8"
	private val dispName_SJIS = "Shift-JIS"

	lateinit var type: TYPE

	constructor(type: TYPE) : this() {
		this.type = type
	}

	constructor(name: String?) : this() {
		if (name.isNullOrEmpty()) {
			this.type = TYPE.SJIS
			return
		}
		when (name) {
			charset_UTF8 -> {
				this.type = TYPE.UTF8
			}
			charset_SJIS -> {
				this.type = TYPE.SJIS
			}
			dispName_UTF8 -> {
				this.type = TYPE.UTF8
			}
			dispName_SJIS -> {
				this.type = TYPE.SJIS
			}
			TYPE.UTF8.toString() -> {
				this.type = TYPE.UTF8
			}
			TYPE.SJIS.toString() -> {
				this.type = TYPE.SJIS
			}
			else -> {
				this.type = TYPE.SJIS
			}
		}
	}

	companion object {
		fun values(): ArrayList<Charset> {
			val list = arrayListOf<Charset>()
			for (value in TYPE.values()) {
				list.add(Charset(value))
			}
			return list
		}

		fun displayNames(): ArrayList<String> {
			val array = arrayListOf<String>()
			for (value in values()) {
				array.add(value.getDisplayName())
			}
			return array
		}
	}

	enum class TYPE {
		UTF8, SJIS;
	}

	/**
	 * 文字コードを取得
	 */
	fun getCode(): String {
		return when (type) {
			TYPE.UTF8 -> {
				charset_UTF8
			}
			TYPE.SJIS -> {
				charset_SJIS
			}
		}
	}

	/**
	 * 表示用ラベルを取得
	 */
	fun getDisplayName(): String {
		return when (type) {
			TYPE.UTF8 -> {
				dispName_UTF8
			}
			TYPE.SJIS -> {
				dispName_SJIS
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