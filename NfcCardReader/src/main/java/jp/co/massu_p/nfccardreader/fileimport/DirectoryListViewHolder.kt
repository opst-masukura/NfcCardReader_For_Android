package jp.co.massu_p.nfccardreader.fileimport

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jp.co.massu_p.nfccardreader.R
import java.io.File

class DirectoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

	interface OnItemClickListener {
		fun onItemClick(file: File)
	}

	val itemImage: ImageView = view.findViewById(R.id.file_type_icon)
	val itemName: TextView = view.findViewById(R.id.file_name)
}