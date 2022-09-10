package com.kudu.tagboard.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kudu.tagboard.R
import com.kudu.tagboard.adapter.HashTagListViewAdapter
import com.kudu.tagboard.databinding.ActivityHashTagBinding
import com.kudu.tagboard.model.HashTags


class HashTagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHashTagBinding

    private var tagList: ArrayList<HashTags> = ArrayList()
    private lateinit var tagListAdapter: HashTagListViewAdapter

    private var mButtonName: String = ""
    private var mButtonId: String = ""

    private val mFirestoreDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHashTagBinding.inflate(layoutInflater)

        setContentView(binding.root)


        if (intent.hasExtra("buttonName")) {
            mButtonName = intent.getStringExtra("buttonName")!!
        }
        if (intent.hasExtra("buttonId")) {
            mButtonId = intent.getStringExtra("buttonId")!!
        }

        setUpActionBar()
        initialiseAdapter()

        binding.btnAddTag.setOnClickListener {
            addTags()
        }

    }

    //delete hashtag
    fun deleteTag(tagId: String, position: Int) {
        mFirestoreDb.collection("hashtags")
            .document(tagId)
            .delete()
            .addOnSuccessListener {
                lifecycleScope.launchWhenCreated {
                    Toast.makeText(this@HashTagActivity,
                        "Tag deleted successfully",
                        Toast.LENGTH_SHORT).show()
                    Log.d("TagDeleted", "Tag successfully deleted!")
                    getAllTags()
                }
            }
            .addOnFailureListener { e ->
                lifecycleScope.launchWhenResumed {
                    Toast.makeText(this@HashTagActivity,
                        "Could not delete the tag. Please try again...",
                        Toast.LENGTH_SHORT).show()
                    Log.e("HashTagDeleteError", "Error while deleting the tag", e)
                }
            }
    }

    //get all hashtags
    private fun getAllTags() {
        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", mButtonId)
            .get()
            /*  .addOnSuccessListener { document ->
                  lifecycleScope.launchWhenCreated {
                      for (i in document.documents) {
                          val tagItem = i.toObject(HashTags::class.java)!!
                          tagItem.buttonId = i.id
                          tagList.add(tagItem)
                      }
                      initialiseAdapter()
                      Toast.makeText(this@HashTagActivity, "Tags Retrieved", Toast.LENGTH_LONG).show()
                  }
              }*/
            .addOnCompleteListener { task ->
                lifecycleScope.launchWhenCreated {
                    if (task.isSuccessful) {
                        var count = 0

                        task.result.forEach { document ->
                            val tag = document.toObject(HashTags::class.java)
                            tag.buttonId = document.id
                            tagList.add(tag)

                            count++
                        }
                        initialiseAdapter()
                        Toast.makeText(this@HashTagActivity, "Tags Retrieved", Toast.LENGTH_LONG)
                            .show()
//                        binding.textViewData.text = count.toString()
                    } else {
                        Log.d("ErrorGetTags", "Error getting all the tags: ", task.exception)
                    }
                }
            }
            .addOnFailureListener { e ->
                lifecycleScope.launchWhenResumed {
                    Toast.makeText(this@HashTagActivity,
                        "Error retrieving tags",
                        Toast.LENGTH_SHORT).show()
                    Log.e("ErrorGetTags", "Error getting all the tags", e)
                }
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
                lifecycleScope.launchWhenCreated {
                    Toast.makeText(this@HashTagActivity,
                        "Successfully added tag",
                        Toast.LENGTH_SHORT).show()
                    Log.i("Added Tag", "$tag added")

                    tagList.clear()
                    getAllTags()
                }
            }
            .addOnFailureListener {
                lifecycleScope.launchWhenResumed {
                    Log.e("ErrorTag", "Error adding tag...")
                }
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

    override fun onStart() {
        super.onStart()
        getAllTags()
        initialiseAdapter()
    }

}