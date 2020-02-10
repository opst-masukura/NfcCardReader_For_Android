package jp.co.massu_p.nfccardreader.views.scanconfirm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.databases.userAssignInfo.UserAssignRecord
import jp.co.massu_p.nfccardreader.models.UserAssignDataModel
import kotlinx.android.synthetic.main.fragment_confirm_view.*

/**
 * スキャン結果確認画面
 *
 * ## 概要
 *
 * スキャン結果から社員情報の確認、修正、保存を行う。
 *
 * ## 機能
 *
 * ### 画面表示
 *
 * DBに一致するTagIdのレコードが存在していればその情報を表示する。
 *
 * 存在していなければ、TagIdとCardIdを同じものとして表示する。
 *
 * ### OKボタンタップ時
 *
 * 入力されたレコードをDBに保存する。
 * TagId以外が未入力でも許容する。
 *
 */
class ConfirmViewFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_confirm_view, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val model = ViewModelProviders.of(this)[UserAssignDataModel::class.java]

		arguments?.let {
			val record = it.getParcelable<UserAssignRecord>(RECORD_EXTRA)
			if (null != record) {
				text_tag_id.setText(record.tagId)
				text_card_id.setText(record.cardId)
				text_employee_id.setText(record.userId)
				text_employee_name.setText(record.userName)
				button_confirm_ok.isEnabled = true
				button_confirm_ok.setOnClickListener {
					record.cardId = text_card_id.text.toString()
					record.userId = text_employee_id.text.toString()
					record.userName = text_employee_name.text.toString()
					model.setRecord(record)
					findNavController().popBackStack()
				}
			}
		}
	}

	companion object {
		val RECORD_EXTRA = "RECORD_EXTRA"
	}
}
