package jp.co.massu_p.nfccardreader.views.mainview


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.co.massu_p.nfccardreader.R

/**
 * A simple [Fragment] subclass.
 */
class EventListFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_event_list, container, false)
	}


}
