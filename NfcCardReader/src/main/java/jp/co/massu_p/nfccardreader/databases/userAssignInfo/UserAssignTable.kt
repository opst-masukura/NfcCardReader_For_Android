package jp.co.massu_p.nfccardreader.databases.userAssignInfo

import jp.co.massu_p.nfccardreader.databases.DbRecord
import jp.co.massu_p.nfccardreader.databases.DbTable

/**
 * ユーザー割り当て情報テーブルの定義
 */
class UserAssignTable(name: String, version: Int, record: DbRecord) : DbTable(name, version, record) {

	companion object {
		const val DB_NAME = "UserAssignInfo"
		const val VERSION = 1
	}
}