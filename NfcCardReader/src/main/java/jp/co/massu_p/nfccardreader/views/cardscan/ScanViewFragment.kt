package jp.co.massu_p.nfccardreader.views.cardscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.co.massu_p.nfccardreader.R


/**
 * カードスキャンの待機画面
 */
class ScanViewFragment : androidx.fragment.app.Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_scan_view, container, false)
	}
}
