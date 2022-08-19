package com.kudu.tagboard.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kudu.tagboard.R
import com.kudu.tagboard.adapter.GroupListAdapter
import com.kudu.tagboard.databinding.ActivityGroupsBinding
import com.kudu.tagboard.databinding.AddGroupDialogBinding
import com.kudu.tagboard.model.ButtonGroupList
import com.kudu.tagboard.model.GroupList

class GroupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupsBinding
    private lateinit var groupListAdapter: GroupListAdapter

    companion object {
        var groupList: ButtonGroupList = ButtonGroupList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        binding.btnAddGroup.setOnClickListener {
            Toast.makeText(this, "Add Group clicked", Toast.LENGTH_SHORT).show()
            customAlertDialog()
        }

        //adapter
        binding.buttonsRv.setItemViewCacheSize(5)
        binding.buttonsRv.layoutManager = LinearLayoutManager(this)
        groupListAdapter = GroupListAdapter(this, groupList = groupList.ref)
        binding.buttonsRv.adapter = groupListAdapter

       /* //on click item adapter item
        groupListAdapter.onItemClick = {
            val intent = Intent(this, AddEditHashtagsActivity::class.java)

        }*/
    }

    //action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarGroupsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_icon_white)
        }
        binding.toolbarGroupsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    //alert dialog
    private fun customAlertDialog() {
        val customDialog =
            LayoutInflater.from(this).inflate(R.layout.add_group_dialog, binding.root, false)
        val binder = AddGroupDialogBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog)
            .setTitle("Add Button Group")
            .setPositiveButton("Add") { dialog, _ ->
                val buttonName = binder.tvGroupNameDialog.text
                if (buttonName != null) {
                    if (buttonName.isNotEmpty()) {
                        addButtonList(buttonName.toString())
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    //adding button list
    private fun addButtonList(name: String) {
        var buttonListExists = false
        for (i in groupList.ref) {
            if (name == i.name) {
                buttonListExists = true
                break
            }
        }
        if (buttonListExists) {
            Toast.makeText(this, "Button Group already exists", Toast.LENGTH_SHORT).show()
        } else {
            val templist = GroupList()
            templist.name = name
            templist.groupList = ArrayList()
            groupList.ref.add(templist)
            groupListAdapter.refreshButtonList()
        }
    }

    override fun onResume() {
        super.onResume()
        groupListAdapter.notifyDataSetChanged()
    }
}