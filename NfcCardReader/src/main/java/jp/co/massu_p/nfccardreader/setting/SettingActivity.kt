package jp.co.massu_p.nfccardreader.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
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

	lateinit private var pref : SharedPreferences

	companion object {
		fun intent(context: Context): Intent =
			Intent(context, SettingActivity::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_setting)

		pref = getSharedPreferences(getString(R.string.pref_name_file_format), Context.MODE_PRIVATE)
		val charset = Charset(pref.getString(getString(R.string.pref_key_charset), null))
		val linefeed = LineFeed(pref.getString(getString(R.string.pref_key_linefeed), null))


		val charsetAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, Charset.displayNames())
		charsetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		spinner_charset.adapter = charsetAdapter
		spinner_charset.setSelection(charset.ordinal())
		spinner_charset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {
			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				val parentSpinner = parent as Spinner
				val selectedCharset = Charset(parentSpinner.selectedItem as String)
				pref.edit().putString(getString(R.string.pref_key_charset), selectedCharset.type.name).apply()
			}

		}

		val linefeedAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, LineFeed.displayNames())
		charsetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		spinner_linefeed.adapter = linefeedAdapter
		spinner_linefeed.setSelection(linefeed.ordinal())
		spinner_linefeed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {
			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				val parentSpinner = parent as Spinner
				val selectedLineFeed = LineFeed(parentSpinner.selectedItem as String)
				pref.edit().putString(getString(R.string.pref_key_linefeed), selectedLineFeed.type.name).apply()
			}

		}
	}
}
