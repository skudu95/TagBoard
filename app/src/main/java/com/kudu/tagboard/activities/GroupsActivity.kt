package com.kudu.tagboard.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kudu.tagboard.R
import com.kudu.tagboard.adapter.ButtonGroupListViewAdapter
import com.kudu.tagboard.databinding.ActivityGroupsBinding
import com.kudu.tagboard.databinding.AddGroupDialogBinding
import com.kudu.tagboard.model.ButtonGroup

class GroupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupsBinding
    private lateinit var buttonListAdapter: ButtonGroupListViewAdapter
    private val buttonList: ArrayList<ButtonGroup> = ArrayList()

    private val mFirestoreDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()
        initialiseAdapter()
        getButtonListFromFirestore()

        //add button
        binding.btnAddGroup.setOnClickListener {
            Toast.makeText(this, "Add Group clicked", Toast.LENGTH_SHORT).show()
            customAlertDialog()
        }
    }

    //create a new button
    private fun addButtonListToFirestore(buttonName: String) {
        val dbRef = mFirestoreDb.collection("buttons")
        val buttonId = dbRef.document().id
        val buttonGroup = ButtonGroup(buttonId, buttonName)

//        dbRef.add(buttonGroup)
        dbRef
            .document()
            .set(buttonGroup, SetOptions.merge()) //changed to check if it works
            .addOnSuccessListener {
                lifecycleScope.launchWhenCreated {
                    Toast.makeText(this@GroupsActivity,
                        "Button added successfully",
                        Toast.LENGTH_SHORT).show()
                    Log.i("Added Button", "$buttonGroup added")

                    getButtonListFromFirestore()
                    buttonListAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                lifecycleScope.launchWhenResumed {
                    Toast.makeText(this@GroupsActivity,
                        "Something went wrong. Please try again...",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    //get list of buttons
    private fun getButtonListFromFirestore() {
        mFirestoreDb.collection("buttons")
            .get()
            /* .addOnSuccessListener { document ->
                 lifecycleScope.launchWhenCreated {
                     Log.e("Button List", document.documents.toString())
                     for (i in document.documents) {
                         val buttonGroup = i.toObject(ButtonGroup::class.java)
                         buttonGroup!!.id = i.id
                         buttonList.add(buttonGroup)
                     }
                     initialiseAdapter()
                 }
             }*/
            .addOnCompleteListener { task ->
                lifecycleScope.launchWhenCreated {
                    /*if (buttonList != null) {
                        buttonList.clear()
                    }*/

                    if (task.isSuccessful) {
                        if (buttonList != null) {
                            buttonList.clear()
                        }

                        for (document in task.result) {
                            val buttonGroup = document.toObject(ButtonGroup::class.java)
                            buttonGroup.id = document.id
                            buttonList.add(buttonGroup)
                        }
                        initialiseAdapter()
                    }
                }
            }
            .addOnFailureListener {
                lifecycleScope.launchWhenResumed {
                    Toast.makeText(this@GroupsActivity,
                        "Error querying button list",
                        Toast.LENGTH_SHORT).show()
                    Log.e("ButtonListError", "Error querying button list")
                }
            }
    }

    //delete button
    fun deleteButtonFromFirestoreList(buttonId: String, position: Int) {
        mFirestoreDb.collection("buttons")
            .document(buttonId)
            .delete()
            .addOnSuccessListener {
                lifecycleScope.launchWhenResumed {
                    Toast.makeText(this@GroupsActivity,
                        "Button deleted successfully",
                        Toast.LENGTH_SHORT).show()
                    getButtonListFromFirestore()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@GroupsActivity,
                    "Could not delete the button. Please try again...",
                    Toast.LENGTH_SHORT).show()
                Log.e("Button Delete Error", "Error while deleting the button", e)

            }
    }

    //initialize adapter
    private fun initialiseAdapter() {
        binding.buttonsRv.layoutManager = LinearLayoutManager(this)
        binding.buttonsRv.setItemViewCacheSize(20)
        binding.buttonsRv.setHasFixedSize(true)
        buttonListAdapter = ButtonGroupListViewAdapter(this, buttonList)
        binding.buttonsRv.adapter = buttonListAdapter
//        buttonListAdapter.notifyDataSetChanged()
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
                        addButtonListToFirestore(buttonName.toString())
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
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

}