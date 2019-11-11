package jp.co.massu_p.nfccardreader.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.file.format.Charset
import jp.co.massu_p.nfccardreader.file.format.LineFeed
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

	companion object {
		fun intent(context: Context): Intent =
			Intent(context, SettingActivity::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_setting)

		val charset = Charset.getCurrent(applicationContext)
		val linefeed = LineFeed.getCurrent(applicationContext)

		val charsetAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, Charset.getDisplayNames())
		charsetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		spinner_charset.adapter = charsetAdapter
		spinner_charset.setSelection(charset.ordinal)
		spinner_charset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {
			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				val parentSpinner = parent as Spinner // TODO 確認
				val selectedCharset = Charset.values()[position]
				Charset.setCurrent(applicationContext, selectedCharset)
			}

		}

		val linefeedAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, LineFeed.displayNames())
		charsetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		spinner_linefeed.adapter = linefeedAdapter
		spinner_linefeed.setSelection(linefeed.ordinal)
		spinner_linefeed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {
			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				val parentSpinner = parent as Spinner // TODO 確認
				val selectedLineFeed = LineFeed.values()[position]
				LineFeed.setCurrent(applicationContext, selectedLineFeed)
			}

		}
	}
}
