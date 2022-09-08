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
    private val buttonList: ArrayList<ButtonGroup>,
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
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_keyboard_button_view, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = buttonList[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_button_name_KL.text = model.buttonName

            /*holder.itemView.setOnClickListener {
                val mFirestoreDb = FirebaseFirestore.getInstance()
                mFirestoreDb.collection("hashtags")
                    .whereEqualTo("buttonId", model.id)
                    .get()
                    .addOnSuccessListener{doc ->
                        var data = ""
                        for(i in doc){
                            val tagsList = i.toObject(HashTags::class.java)
                            val tagName = arrayOf(tagsList.tagName)

                            for(tagList in tagName){
                                data += " $tagList"
                            }

                        }
                    }
            }*/

        }
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

}