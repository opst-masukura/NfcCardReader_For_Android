package jp.co.massu_p.nfccardreader.fileimport

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import jp.co.massu_p.nfccardreader.R
import java.io.File

class FileImportActivity : AppCompatActivity(), FileListFragment.onClickItemListener {

	companion object {
		fun intent(context: Context): Intent =
			Intent(context, FileImportActivity::class.java)
	}

	private lateinit var currentFile: File

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_file_import)

		currentFile = File(Environment.getExternalStorageDirectory().path)

		if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			if (savedInstanceState == null) {
				val transaction = supportFragmentManager.beginTransaction()
				transaction.add(R.id.view_container, FileListFragment.newInstance(currentFile))
				transaction.commit()
			}
		} else {
			requestPermissions(
				arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
			)
		}


	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			1 -> {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 許可の場合。実装は後で。
				} else {
					// 不許可の場合。実装は後で。
				}
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	override fun onResume() {
		super.onResume()

	}

	override fun onItemClick(file: File) {
		if (file == currentFile.parentFile) {
			return
		}
		if (file.isDirectory) {
			val transaction = supportFragmentManager.beginTransaction()
			transaction.replace(R.id.view_container, FileListFragment.newInstance(file))
			transaction.commit()
		}
	}
}

