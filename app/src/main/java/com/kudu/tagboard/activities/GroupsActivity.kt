package com.kudu.tagboard.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
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
        /*getButtonListFromFirestore()
        initialiseAdapter()*/

        //add button
        binding.btnAddGroup.setOnClickListener {
            Toast.makeText(this, "Add Group clicked", Toast.LENGTH_SHORT).show()
            customAlertDialog()
        }
        getButtonListFromFirestore()
        initialiseAdapter()
    }

    //create a new button
    private fun addButtonListToFirestore(buttonName: String) {
        val dbRef = mFirestoreDb.collection("buttons")
        val buttonId = dbRef.document().id
//        val buttonGroup = ButtonGroup(dbRef.id,buttonName)
//        val buttonGroup = ButtonGroup("", buttonName)
        val buttonGroup = ButtonGroup(buttonId, buttonName)
        dbRef.add(buttonGroup)
            .addOnSuccessListener {
                Toast.makeText(this, "Button added successfully", Toast.LENGTH_SHORT).show()
                Log.i("Added Button", "$buttonGroup added")

                /*   buttonList.clear()
                   getButtonListFromFirestore() //TODO: delete if not used*/
            }
            .addOnFailureListener {
                Toast.makeText(this,
                    "Something went wrong. Please try again...",
                    Toast.LENGTH_SHORT).show()
            }
        getButtonListFromFirestore()
    }

    //get list of buttons
//    private fun getButtonListFromFirestore() {
    private fun getButtonListFromFirestore() {
        mFirestoreDb.collection("buttons")
            .get()
            .addOnSuccessListener { document ->
                Log.e("Button List", document.documents.toString())
                for (i in document.documents) {
                    val buttonGroup = i.toObject(ButtonGroup::class.java)
                    buttonGroup!!.id = i.id
//                    buttonList.clear()
                    buttonList.add(buttonGroup)
                }
                initialiseAdapter()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error querying button list", Toast.LENGTH_SHORT).show()
                Log.e("ButtonListError", "Error querying button list")
            }
    }
    /*   private fun getButtonListFromFirestore() {
           val buttonRef = mFirestoreDb.collection("buttons").limit(20)

           buttonRef.addSnapshotListener { snapshot, exception ->
               if (exception != null || snapshot == null) {
                   Log.e(TAG, "Exception when querying buttons", exception)
                   return@addSnapshotListener
               }

               val buttonGroup = snapshot.toObjects(ButtonGroup::class.java)
               buttonList.clear()
               buttonList.addAll(buttonGroup)
               for (button in buttonGroup) {
                   Log.d(TAG, "Button: $button")
               }
           }
       }*/

    fun deleteButtonFromFirestoreList(buttonId: String) {
        mFirestoreDb.collection("buttons")
            .document(buttonId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Button deleted successfully", Toast.LENGTH_SHORT).show()
                buttonList.clear()
//                getButtonListFromFirestore()
                initialiseAdapter()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,
                    "Could not delete the button. Please try again...",
                    Toast.LENGTH_SHORT).show()
                Log.e("Button Delete Error", "Error while deleting the button", e)
            }
    }

    //initialize adapter
    private fun initialiseAdapter() {
        binding.buttonsRv.setItemViewCacheSize(20)
        binding.buttonsRv.setHasFixedSize(true)
        binding.buttonsRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        buttonListAdapter = ButtonGroupListViewAdapter(this, buttonList)
        binding.buttonsRv.adapter = buttonListAdapter
        buttonListAdapter.notifyDataSetChanged()
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
                buttonListAdapter.notifyDataSetChanged()
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

    override fun onResume() {
        super.onResume()
//        getButtonListFromFirestore()
//        buttonListAdapter.notifyDataSetChanged()
        initialiseAdapter()
    }
}