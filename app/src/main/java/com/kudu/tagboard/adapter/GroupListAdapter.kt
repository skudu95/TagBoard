package com.kudu.tagboard.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kudu.tagboard.activities.AddEditHashtagsActivity
import com.kudu.tagboard.activities.GroupsActivity
import com.kudu.tagboard.databinding.ItemGroupsListLayoutBinding
import com.kudu.tagboard.model.GroupList

class GroupListAdapter(
    private val context: Context,
    private var groupList: ArrayList<GroupList>,
) : RecyclerView.Adapter<GroupListAdapter.MyViewHolder>() {

//    var onItemClick: ((GroupList) -> Unit)? = null

    class MyViewHolder(binding: ItemGroupsListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val buttonName = binding.tvButtonName
        val deleteButton = binding.btnDeleteGroup
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemGroupsListLayoutBinding.inflate(LayoutInflater.from(context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = groupList[position]

        holder.buttonName.text = model.name
        holder.deleteButton.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setMessage("Do you want to delete the button group?")
                .setPositiveButton("Yes") { dialog, _ ->
                    GroupsActivity.groupList.ref.removeAt(position)
                    refreshButtonList()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
        }
        holder.root.setOnClickListener {
            val intent = Intent(context, AddEditHashtagsActivity::class.java)
            intent.putExtra("index", position)
            ContextCompat.startActivity(context, intent, null)
        }

        /*   holder.itemView.setOnClickListener {
               val intent = Intent(context, AddEditHashtagsActivity::class.java)
   //            intent.putExtra("index", position)
               context.startActivity(intent)
   //            ContextCompat.startActivity(context, intent, null)
           }*/
        /*holder.itemView.setOnClickListener{
            onItemClick?.invoke(model)
        }*/

    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    fun refreshButtonList() {
        groupList = ArrayList()
        groupList.addAll(GroupsActivity.groupList.ref)
        notifyDataSetChanged()
    }


}