package jp.co.massu_p.nfccardreader.views.mainview


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

import jp.co.massu_p.nfccardreader.R

/**
 * メイン画面
 */
class MainFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val mainView = inflater.inflate(R.layout.fragment_main, container, false)
		mainView.findViewById<Button>(R.id.btn_scan_start).setOnClickListener{
			findNavController().navigate(R.id.toScanCard)
		}
		return mainView
	}
}
