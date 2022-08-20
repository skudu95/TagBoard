package com.kudu.tagboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kudu.tagboard.databinding.ItemHashtagListLayoutBinding
import com.kudu.tagboard.model.Group

class HashtagAdapter(
    private val context: Context,
    var hashtagList: ArrayList<Group>, //change this to list of groups or hashtags
//    var hashtagList: ArrayList<String>, //change this to list of groups or hashtags
) : RecyclerView.Adapter<HashtagAdapter.MyViewHolder>() {

    class MyViewHolder(binding: ItemHashtagListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tagName = binding.tvHashtag
        val deleteTag = binding.btnDeleteGroup

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemHashtagListLayoutBinding.inflate(LayoutInflater.from(
            context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tagModel = hashtagList[position]

//        holder.tagName.text = tagModel.hashtags.toString()
        holder.tagName.text = tagModel.hashtags[position]
        holder.deleteTag.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setMessage("Do you want to delete the tag?")
                .setPositiveButton("Yes") { dialog, _ ->
//                    GroupsActivity.groupList.ref.removeAt(position)
//                    refreshTagList()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
        }

    }

    override fun getItemCount(): Int {
        return hashtagList.size
    }

    fun refreshTagList(){
        hashtagList = ArrayList()
//        hashtagList.addAll(AddEditHashtagsActivity.)
    }
}