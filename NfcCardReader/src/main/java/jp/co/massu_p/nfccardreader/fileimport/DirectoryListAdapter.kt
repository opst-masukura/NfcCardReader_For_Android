package jp.co.massu_p.nfccardreader.fileimport

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.co.massu_p.nfccardreader.R
import java.io.File

class DirectoryListAdapter(
	private val context: Context,
	private val itemList: Array<File>,
	private val itemClickListener: DirectoryListViewHolder.OnItemClickListener
) : RecyclerView.Adapter<DirectoryListViewHolder>() {

	private var recyclerView: RecyclerView? = null

	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		this.recyclerView = recyclerView
	}

	override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
		super.onDetachedFromRecyclerView(recyclerView)
		this.recyclerView = null
	}

	override fun onCreateViewHolder(parent: ViewGroup, type: Int): DirectoryListViewHolder {
		val layoutInflater = LayoutInflater.from(context)
		val mainView = layoutInflater.inflate(R.layout.directory_list_item, parent, false)

		mainView.setOnClickListener { view ->
			recyclerView?.let {
				itemClickListener.onItemClick(itemList.get(it.getChildAdapterPosition(view)))
			}
		}

		return DirectoryListViewHolder(mainView)
	}

	override fun getItemCount(): Int {
		return itemList.size
	}

	override fun onBindViewHolder(holder: DirectoryListViewHolder, position: Int) {
		holder.let {
			val item = itemList.get(position)
			if (item.isDirectory) {
				it.itemImage.setImageResource(R.drawable.baseline_folder_open_black_48)
			} else {
				it.itemImage.setImageResource(R.drawable.outline_insert_drive_file_black_48)
			}
			it.itemName.text = item.name
		}
	}
}