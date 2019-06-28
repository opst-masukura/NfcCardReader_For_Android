package jp.co.massu_p.nfccardreader.cardscan

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.co.massu_p.nfccardreader.R


/**
 * NFCのスキャン待機中の表示を行う
 */
class ScanViewFragment : Fragment() {
	private var listener: OnFragmentInteractionListener? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_scan_view, container, false)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is OnFragmentInteractionListener) {
			listener = context
		} else {
			throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
		}
	}

	override fun onDetach() {
		super.onDetach()
		listener = null
	}

	interface OnFragmentInteractionListener {
		fun onFragmentInteraction(uri: Uri)
	}

	companion object {
		@JvmStatic
		fun newInstance() = ScanViewFragment()
	}
}
