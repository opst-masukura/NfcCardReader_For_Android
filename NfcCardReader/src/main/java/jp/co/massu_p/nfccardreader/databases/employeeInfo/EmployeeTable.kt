package jp.co.massu_p.nfccardreader.databases.employeeInfo

import jp.co.massu_p.nfccardreader.databases.DbRecord
import jp.co.massu_p.nfccardreader.databases.DbTable

/**
 * 社員情報テーブルの定義
 */
class EmployeeTable(name: String, version: Int, record: DbRecord) : DbTable(name, version, record) {

	companion object {
		const val DB_NAME = "EmployeeInfo"
		const val VERSION = 1
	}
}