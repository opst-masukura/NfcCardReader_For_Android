package jp.co.massu_p.nfccardreader.fileimport

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import jp.co.massu_p.nfccardreader.R

import java.io.File

class FileImportFragment : Fragment(), DirectoryListViewHolder.OnItemClickListener {

	private val rootPath = File(Environment.getExternalStorageDirectory().path).path

	private lateinit var currentFile: File

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			currentFile = it.getSerializable(FILE_EXTRA) as File
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val mainView = inflater.inflate(R.layout.fragment_filelist_list, container, false)
		val btnReturnParent = mainView.findViewById<ImageView>(R.id.btn_return_parent)
		btnReturnParent.setOnClickListener { view ->
			btnReturnParent?.let {
				listener?.onItemClick(currentFile.parentFile)
			}
		}

		val directoryName = mainView.findViewById<TextView>(R.id.current_directory_name)
		if (currentFile.path == rootPath) {
			directoryName.text = "/"
		} else {
			// FIXME パスが長すぎると後ろの方が見えなくなっちゃう
			val path = currentFile.path.replace(rootPath, "")
			directoryName.text = path
		}

		val directoryList = mainView.findViewById(R.id.directory_list) as RecyclerView
		val textNoFile = mainView.findViewById<TextView>(R.id.text_no_file)
		if (currentFile.listFiles().isEmpty()) {
			directoryList.visibility = View.GONE
			textNoFile.visibility = View.VISIBLE
		} else {
			directoryList.adapter = DirectoryListAdapter(context!!, currentFile.listFiles(), this)
			textNoFile.visibility = View.GONE
		}

		return mainView
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is OnClickItemListener) {
			listener = context
		} else {
			throw RuntimeException("$context must implement OnFragmentInteractionListener")
		}
	}

	override fun onDetach() {
		super.onDetach()
		listener = null
	}

	override fun onItemClick(file: File) {
		listener?.onItemClick(file)
	}

	private var listener: OnClickItemListener? = null

	interface OnClickItemListener {
		fun onItemClick(file: File)
	}

	companion object {
		private const val FILE_EXTRA = "FILE_EXTRA"

		@JvmStatic
		fun newInstance(file: File) = FileImportFragment().apply {
			arguments = Bundle().apply {
				putSerializable(FILE_EXTRA, file)
			}
		}
	}
}
