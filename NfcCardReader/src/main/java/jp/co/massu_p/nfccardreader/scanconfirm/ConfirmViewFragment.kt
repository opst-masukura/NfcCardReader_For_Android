package jp.co.massu_p.nfccardreader.scanconfirm

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.databases.employeeInfo.EmployeeRecord
import kotlinx.android.synthetic.main.fragment_confirm_view.*

/**
 * カードIDの確認を行う画面
 *
 * ## 概要
 *
 * 社員情報の表示と修正を行う画面。
 *
 * ## 機能
 *
 * 1. カードIDの変更を行える。カードIDは英数字を受け付ける。小文字は大文字に変換され保存される。
 * 1. 社員番号の変更を行える。社員番号は英数字を受け付ける。大文字は小文字に変換され保存される。
 * 1. 社員名の変更を行える。
 *
 */
class ConfirmViewFragment : Fragment() {

	private var listener: OnFragmentInteractionListener? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_confirm_view, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		arguments?.let {
			val record = it.getParcelable<EmployeeRecord>(RECORD_EXTRA)
			if (null != record) {
				text_tag_id.setText(record.tagId)
				text_card_id.setText(record.cardId)
				text_employee_id.setText(record.employeeId)
				text_employee_name.setText(record.employeeName)
				button_confirm_ok.setOnClickListener {
					record.cardId = text_card_id.text.toString()
					record.employeeId = text_employee_id.text.toString()
					record.employeeName = text_employee_name.text.toString()
					if (listener != null) listener?.onConfirmOk(record)
				}
			}
		}
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
		fun onConfirmOk(record: EmployeeRecord)
	}

	companion object {
		private val RECORD_EXTRA = "RECORD_EXTRA"

		@JvmStatic
		fun newInstance(record: EmployeeRecord) =
			ConfirmViewFragment().apply {
				arguments = Bundle().apply {
					putParcelable(RECORD_EXTRA, record)
				}
			}
	}
}
