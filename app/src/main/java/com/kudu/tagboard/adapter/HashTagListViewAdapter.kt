package com.kudu.tagboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kudu.tagboard.R
import com.kudu.tagboard.activities.HashTagActivity
import com.kudu.tagboard.model.HashTags
import kotlinx.android.synthetic.main.item_hashtag_list_layout.view.*

class HashTagListViewAdapter(
    private val context: Context,
    private val hashTagList: ArrayList<HashTags>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_hashtag_list_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = hashTagList[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_hashtag_name.text = model.tagName
            holder.itemView.btn_delete_hashtag.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(context)
                builder.setMessage("Do you want to delete the tag?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        HashTagActivity().deleteTag(model.buttonId!!)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val customDialog = builder.create()
                customDialog.show()
//                HashTagActivity().showAlertDialogToDeleteTag(model.buttonId!!)
            }
        }

    }

    override fun getItemCount(): Int {
        return hashTagList.size
    }
}