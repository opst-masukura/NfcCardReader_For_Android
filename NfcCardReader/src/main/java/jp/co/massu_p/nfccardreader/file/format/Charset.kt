package jp.co.massu_p.nfccardreader.file.format

import android.content.Context
import jp.co.massu_p.nfccardreader.R

/**
 * 文字コードの定義
 */
enum class Charset {

	UTF8 {
		override val code: String
			get() = "UTF-8"
		override val dispName: String
			get() = "UTF-8"
	},
	SJIS {
		override val code: String
			get() = "windows-31j"
		override val dispName: String
			get() = "Shift-JIS"
	};

	abstract val code: String
	abstract val dispName: String

	companion object {
		val default = SJIS
		private const val pref_key = "pref_key_charset"

		fun getCurrent(context: Context) : Charset {
			val pref = context.getSharedPreferences(context.getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)
			val name = pref.getString(pref_key, SJIS.toString())
			for (value in values()) {
				if (value.toString() == name) {
					return value
				}
			}
			return default
		}

		fun setCurrent(context: Context, value: Charset) {
			val pref = context.getSharedPreferences(context.getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)
			pref.edit().putString(pref_key, value.toString()).apply()
		}

		fun getDisplayNames(): ArrayList<String> {
			val array = arrayListOf<String>()
			for (value in values()) {
				array.add(value.dispName)
			}
			return array
		}
	}
}