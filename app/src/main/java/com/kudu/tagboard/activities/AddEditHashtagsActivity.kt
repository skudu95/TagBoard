package com.kudu.tagboard.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kudu.tagboard.R
import com.kudu.tagboard.adapter.HashtagAdapter
import com.kudu.tagboard.databinding.ActivityAddEditHashtagsBinding
import com.kudu.tagboard.databinding.AddHashtagDialogBinding

class AddEditHashtagsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditHashtagsBinding
    private lateinit var hashtagListAdapter: HashtagAdapter
    var hashtagList: ArrayList<String> = ArrayList()
    var hashtagPosition = 0

    /*companion object {
        var hashtagList: ArrayList<String> = ArrayList()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditHashtagsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        //adapter
        binding.tagsRv.setItemViewCacheSize(30)
        binding.tagsRv.layoutManager = LinearLayoutManager(this)
//        hashtagListAdapter = HashtagAdapter(this, hashtagList )
    }

    //action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarAddEditHashtagsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_icon_white)
        }
        binding.toolbarAddEditHashtagsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    //alert dialog
    private fun customAlertDialog() {
        val customDialog =
            LayoutInflater.from(this).inflate(R.layout.add_hashtag_dialog, binding.root, false)
        val binder = AddHashtagDialogBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog)
            .setTitle("Add Tag")
            .setPositiveButton("Add") { dialog, _ ->
                val tagName = binder.tvHashtagDialog.text
                if (tagName != null) {
                    if (tagName.isNotEmpty()) {
//                        addTagList(tagName.toString())
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun addTagList(name: String, position: Int) {
        /*var hashtagListExists = false
        for (i in hashtagList){
            if (name == i.)
        }*/
    }
}