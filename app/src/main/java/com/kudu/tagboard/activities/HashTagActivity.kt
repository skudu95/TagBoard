package com.kudu.tagboard.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kudu.tagboard.R
import com.kudu.tagboard.adapter.HashTagListViewAdapter
import com.kudu.tagboard.databinding.ActivityHashTagBinding
import com.kudu.tagboard.model.HashTags

class HashTagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHashTagBinding

    private val tagList: ArrayList<HashTags> = ArrayList()
    private lateinit var tagListAdapter: HashTagListViewAdapter
//    private var currentGroupListPosition: Int = -1

    private var mButtonName: String = ""
    var mButtonId: String = ""

    //    private var mButtonId: String = ""
    private val mFirestoreDb = FirebaseFirestore.getInstance()
    private val tagListNames: ArrayList<String> = ArrayList()

//    val buttonGroup: ButtonGroup? = intent.getSerializableExtra("index") as ButtonGroup?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHashTagBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        currentGroupListPosition = intent.extras?.get("index") as Int
//        val buttonGroup: ButtonGroup? = intent.getSerializableExtra("index") as ButtonGroup?

        if (intent.hasExtra("buttonName")) {
            mButtonName = intent.getStringExtra("buttonName")!!
        }
        if (intent.hasExtra("buttonId")) {
            mButtonId = intent.getStringExtra("buttonId")!!
        }

        setUpActionBar()
        getAllTags()
        getTagNameList()

        binding.btnAddTag.setOnClickListener {
            addTags()
        }

    }

    //delete hashtag
    fun deleteTag(tagId: String) {
        mFirestoreDb.collection("hashtags")
            .document(tagId)
            .delete()
            .addOnSuccessListener {
//                Toast.makeText(this, "Tag deleted successfully", Toast.LENGTH_SHORT).show()
//                tagList.clear()
                getAllTags()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,
                    "Could not delete the tag. Please try again...",
                    Toast.LENGTH_SHORT).show()
                Log.e("HashTagDeleteError", "Error while deleting the tag", e)
            }
    }

    //custom alert for delete tag
    fun showAlertDialogToDeleteTags(tagId: String) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setMessage("Do you want to delete the tag?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteTag(tagId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val customDialog = builder.create()
        customDialog.setCancelable(false)
        customDialog.show()
    }

    fun showAlertDialogToDeleteTag(tagId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to delete the tag?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteTag(tagId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val customDialog: AlertDialog = builder.create()
        customDialog.setCancelable(false)
        customDialog.show()
    }

    private fun getTagNameList() {
        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", mButtonId)
            .get()
            .addOnSuccessListener { document ->
                var data = ""
                for (i in document) {
                    val tag = i.toObject(HashTags::class.java)
                    val tagName = arrayOf(tag.tagName)

                    for (tag in tagName) {
//                        data += tagName.joinToString(" ")
                        data += " $tag"
                    }


                    /* for(tag in tagName!!){
 //                        data += "\n$tag"
                         data += "$tag"
                     }*/
//                    data += "\n\n"
                }
                binding.textViewData.text = data
            }
    }


    //get all hashtags
    private fun getAllTags() {
        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", mButtonId)
            .get()
            .addOnSuccessListener { document ->
                for (i in document.documents) {
                    val tagItem = i.toObject(HashTags::class.java)!!
                    tagItem.buttonId = i.id
                    tagList.add(tagItem)
                }
                initialiseAdapter()
                Toast.makeText(this, "Tags Retrieved", Toast.LENGTH_LONG).show()

                /*val joinedData = tagList.joinToString (" ")
                binding.textViewData.text = joinedData*/

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving tags", Toast.LENGTH_SHORT).show()
                Log.e("ErrorGetTags", "Error getting all the tags", e)
            }
    }

    //add hash tags
    private fun addTags() {
        val tagName = binding.etInputTag.text.toString()
        val tag = HashTags(mButtonId, tagName)
        val dbRef = mFirestoreDb.collection("hashtags")

        dbRef
            .document()
            .set(tag, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully added tag", Toast.LENGTH_SHORT).show()
                Log.i("Added Tag", "$tag added")

                tagList.clear()
                getAllTags()
            }
            .addOnFailureListener {
                Log.e("ErrorTag", "Error adding tag...")
            }
    }

    //initialising adapter
    private fun initialiseAdapter() {
        binding.tagsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.tagsRv.setItemViewCacheSize(20)
        binding.tagsRv.setHasFixedSize(true)
        tagListAdapter = HashTagListViewAdapter(this, tagList)
        binding.tagsRv.adapter = tagListAdapter
//        tagListAdapter.notifyDataSetChanged()

    }

    //action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarHashtagsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_icon_white)
        }
        binding.tvTitle.text = mButtonName
        binding.toolbarHashtagsActivity.setNavigationOnClickListener { onBackPressed() }
    }

}