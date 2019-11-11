package jp.co.massu_p.nfccardreader.file.format

import android.content.Context
import jp.co.massu_p.nfccardreader.R

/**
 * 改行コードの定義
 */
enum class LineFeed {

	CR {
		override val code: String
			get() = "\r"
	},
	LF {
		override val code: String
			get() = "\n"
	},
	CRLF {
		override val code: String
			get() = "\r\n"
	};

	abstract val code: String

	companion object {
		val default = CRLF
		private const val pref_key = "pref_key_linefeed"

		fun getCurrent(context: Context) : LineFeed {
			val pref = context.getSharedPreferences(context.getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)
			val name = pref.getString(pref_key, CRLF.toString())
			for (value in values()) {
				if (value.toString() == name) {
					return value
				}
			}
			return default
		}

		fun setCurrent(context: Context, value: LineFeed) {
			val pref = context.getSharedPreferences(context.getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)
			pref.edit().putString(pref_key, value.toString()).apply()
		}

		fun displayNames(): ArrayList<String> {
			val array = arrayListOf<String>()
			for (value in values()) {
				array.add(value.toString())
			}
			return array
		}
	}
}