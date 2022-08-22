package com.kudu.tagboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kudu.tagboard.databinding.ItemKeyboardButtonView2Binding

open class KeyboardButtonListAdapter(
    val context: Context,
//    private var groupList: ArrayList<GroupList>,
    var groupList: ArrayList<String>,
) : RecyclerView.Adapter<KeyboardButtonListAdapter.MyViewHolder>() {


    class MyViewHolder(binding: ItemKeyboardButtonView2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        val buttonName = binding.tvButtonNameKL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemKeyboardButtonView2Binding.inflate(LayoutInflater.from(context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = groupList[position]

        holder.buttonName.text = model

    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    /* fun refreshButtonList() {
         groupList = ArrayList()
         groupList.addAll(GroupsActivity.groupList.ref)
         notifyDataSetChanged()
     }
 */
}
