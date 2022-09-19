package com.kudu.tagboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kudu.tagboard.R
import com.kudu.tagboard.activities.GroupsActivity
import com.kudu.tagboard.model.ButtonGroup
import kotlinx.android.synthetic.main.item_groups_list_layout.view.*

class SettingAdapter(
    private val context: Context,
    private val groupList: ArrayList<ButtonGroup>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_groups_list_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = groupList[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_button_name.text = model.buttonName
            holder.itemView.btn_delete_group.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(context)
                builder.setMessage("Do you want to delete the button?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        GroupsActivity().deleteButtonFromFirestoreList(model.id!!, position)
                        groupList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, groupList.size)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val customDialog = builder.create()
                customDialog.show()
            }
        }

    }

    override fun getItemCount(): Int {
        return groupList.size
    }

}