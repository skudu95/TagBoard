package com.kudu.tagboard.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kudu.tagboard.R
import com.kudu.tagboard.databinding.ActivityGroupsBinding

class GroupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        binding.btnAddGroup.setOnClickListener {
//            customAlertDialog()
        }
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
    /*private fun customAlertDialog() {
        val customDialog = LayoutInflater.from(this@GroupsActivity)
            .inflate(R.layout.add_group_dialog, binding.root, false)
        val binder = AddGroupDialogBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this, R.style.customDialogTheme)
        builder.setView(customDialog)
            .setTitle("Group Details")
            .setPositiveButton("Add") { dialog, _ -> // 1st _ dialog, 2nd _ result
                val groupName = binder.groupNameDialog.text
                if (groupName != null )
                    if (groupName.isNotEmpty() ) {
                        addPlaylist(groupName.toString())
                    }
                dialog.dismiss()
            }
            .show()
    }*/

    //add group
    /*private fun addGroup(name: String){
        var groupExists = false
        for(i in )
    }*/
}