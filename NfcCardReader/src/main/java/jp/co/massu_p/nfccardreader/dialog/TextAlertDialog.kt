package jp.co.massu_p.nfccardreader.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import jp.co.massu_p.nfccardreader.R

class TextAlertDialog(context: Context) {

	private val dialog = DialogBody(context)

	var title: String
		get() {
			return dialog.title
		}
		set(value) {
			dialog.title = value
		}

	var message: String
		get() {
			return dialog.message
		}
		set(value) {
			dialog.message = value
		}

	var onOkClickListener: DialogInterface.OnClickListener
		get() {
			// 実際には使用しない
			return DialogInterface.OnClickListener { _, _ -> }
		}
		set(value) {
			dialog.onOkClickListener = value
		}

	var onCancelClickListener: DialogInterface.OnClickListener
		get() {
			// 実際には使用しない
			return DialogInterface.OnClickListener { _, _ -> }
		}
		set(value) {
			dialog.onCancelClickListener = value
		}

	fun showDialog(manager: FragmentManager) {
		dialog.show(manager, "dialog")
	}

	class DialogBody(context: Context) : DialogFragment() {
		var title: String = context.getString(R.string.label_default_title)
		var message: String = context.getString(R.string.label_default_message)
		var onOkClickListener: DialogInterface.OnClickListener =
			DialogInterface.OnClickListener { _, _ -> }
		var onCancelClickListener: DialogInterface.OnClickListener? = null

		override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
			val builder = AlertDialog.Builder(activity!!)
			builder.setTitle(title)
			builder.setMessage(message)
			builder.setPositiveButton(android.R.string.ok, onOkClickListener)
			onCancelClickListener?.let { builder.setNegativeButton(android.R.string.cancel, it) }
			return builder.create()
		}

		override fun onPause() {
			super.onPause()
			dismiss()
		}
	}
}