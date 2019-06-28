package jp.co.massu_p.nfccardreader.utils

import android.nfc.Tag
import java.lang.StringBuilder

object Extensions {

	/**
	 * TAGのIDを16進数表記の文字列にして返却する
	 *
	 * @return ID文字列
	 */
	fun Tag.getTagId(): String {
		val tagBuilder = StringBuilder()
		for (hex in id) {
			tagBuilder.append(String.format("%02X", hex))
		}
		return tagBuilder.toString()
	}

}