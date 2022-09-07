package com.kudu.tagboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kudu.tagboard.R
import com.kudu.tagboard.model.ButtonGroup
import kotlinx.android.synthetic.main.item_keyboard_button_view.view.*

class KeyboardViewAdapter(
    private val context: Context,
    private val buttonList: ArrayList<ButtonGroup>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_keyboard_button_view, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = buttonList[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_button_name_KL.text = model.buttonName
        }
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }
}