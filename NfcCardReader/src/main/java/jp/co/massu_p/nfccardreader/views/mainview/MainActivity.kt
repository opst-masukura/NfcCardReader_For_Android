package jp.co.massu_p.nfccardreader.views.mainview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.co.massu_p.nfccardreader.R

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if (savedInstanceState == null) {
			val transaction = supportFragmentManager.beginTransaction()
			transaction.add(R.id.view_container, MainFragment.newInstance())
			transaction.commit()
		}
	}
}
