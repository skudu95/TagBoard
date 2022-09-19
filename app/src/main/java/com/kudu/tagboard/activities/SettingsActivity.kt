package com.kudu.tagboard.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.tagboard.adapter.SettingAdapter
import com.kudu.tagboard.databinding.ActivitySettingsBinding
import com.kudu.tagboard.model.ButtonGroup
import com.kudu.tagboard.model.HashTags


class SettingsActivity : AppCompatActivity(), SettingAdapter.OnItemClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var buttonListAdapter: SettingAdapter
    private val buttonList: ArrayList<ButtonGroup> = ArrayList()
//    var ic: InputConnection = editText.onCreateInputConnection(EditorInfo())

    var progressCount = 0
    private val mFirestoreDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialiseAdapter()
        getButtonListFromFirestore()

        val seekBar: SeekBar = binding.tagSeekbar
        val seekText = binding.tagSeekbarText

        //seekbar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                val strProgress = progress.toString()
                if (fromUser) {
                    seekText.text = "$strProgress Tags"
                    /*getButtonListFromSeekbar(progress)
                    buttonListAdapter.notifyDataSetChanged()*/

                    progressCount = progress

                    /*mFirestoreDb.collection("hashtags")
                        .get()
                        .addOnSuccessListener { doc ->
                            var data = ""
                            for (i in doc) {
                                val tagsList = i.toObject(HashTags::class.java)
                                val tagName = arrayOf(tagsList.tagName)

                                for (tagList in tagName) {
                                    data += " $tagList"
                                }
                            }
                            binding.etInputView.text.clear()
                            val ic: InputConnection =
                                binding.etInputView.onCreateInputConnection(EditorInfo())
                            ic.commitText(data, 1)
                            Log.d("ClickKeyData", data)
                        }*/
                }
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekbar: SeekBar?) = Unit

        })
    }

    private fun getButtonListFromFirestore() {
        mFirestoreDb.collection("buttons")
            .get()
            .addOnCompleteListener { task ->
                lifecycleScope.launchWhenCreated {
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
                    Toast.makeText(this@SettingsActivity,
                        "Error querying button list",
                        Toast.LENGTH_SHORT).show()
                    Log.e("ButtonListError", "Error querying button list")
                }
            }
    }

    fun getButtonListFromSeekbar(count: Int) {
        mFirestoreDb.collection("buttons")
            .whereEqualTo("tagItemsNumber", count)
            .get()
            .addOnCompleteListener { task ->
                lifecycleScope.launchWhenCreated {
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
                    Toast.makeText(this@SettingsActivity,
                        "Error querying button list",
                        Toast.LENGTH_SHORT).show()
                    Log.e("ButtonListError", "Error querying button list")
                }
            }
    }

    //initialize adapter
    private fun initialiseAdapter() {
        binding.buttonsRv.layoutManager = LinearLayoutManager(this)
        binding.buttonsRv.setItemViewCacheSize(20)
        binding.buttonsRv.setHasFixedSize(true)
        buttonListAdapter = SettingAdapter(this, buttonList, this)
        binding.buttonsRv.adapter = buttonListAdapter
    }

    @SuppressLint("NewApi")
    override fun onItemClick(position: Int) {
        val clickedItem: ButtonGroup = buttonList[position]
        val clickedButtonId = clickedItem.id

        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", clickedButtonId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val tagList: MutableList<String> = mutableListOf()
                    for (i in task.result) {
                        val tag = i.toObject(HashTags::class.java)
                        val hashtag = tag.tagName
                        tagList.add(hashtag.toString())
                        Log.d("hashtag", hashtag.toString())
                    }

                    val shuffledTagList =
                        tagList.asSequence().shuffled().take(progressCount).toMutableList()
                    val noCommaBracket =
                        shuffledTagList.toString().replace("[", "").replace("]", "")
                            .replace(",", "")

                    Log.d("noCommaBracket", noCommaBracket)
                    val tagListSize = tagList.size
                    Log.d("tagListSize", tagListSize.toString())

                    binding.etInputView.text.clear()
                    val ic: InputConnection =
                        binding.etInputView.onCreateInputConnection(EditorInfo())
                    ic.commitText(noCommaBracket, 1)
                    Log.d("ClickKeyData", noCommaBracket)
                }
            }
    }

    /* override fun onItemClick(position: Int) {

         val clickedItem: ButtonGroup = buttonList[position]
         val clickedButtonId = clickedItem.id

         val mFirestoreDb = FirebaseFirestore.getInstance()
         mFirestoreDb.collection("hashtags")
             .whereEqualTo("buttonId", clickedButtonId)
 //            .limit(progressCount.toLong()) //TODO: make changes here
             .get()
             .addOnSuccessListener { doc ->
                 var data = ""
                 for (i in doc) {
                     val tagsList = i.toObject(HashTags::class.java)
 //                    val tagName = listOf(tagsList.tagName).asSequence().shuffled().take(progressCount)
                     val tagName = arrayOf(tagsList.tagName)


                     for (tagList in tagName) {
                         data += " $tagList"
                     }
                 }

                 binding.etInputView.text.clear()
                 val ic: InputConnection = binding.etInputView.onCreateInputConnection(EditorInfo())
                 ic.commitText(data, 1)
                 Log.d("ClickKeyData", data)
             }
     }*/

    /*override fun onItemClick(position: Int) {
        val clickedItem: ButtonGroup = buttonList[position]
        val clickedButtonId = clickedItem.id

        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", clickedButtonId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val tagList: MutableList<HashTags?> = ArrayList()
                    for (document in task.result) {
                        val tag: HashTags = document.toObject(HashTags::class.java)
                        tagList.add(tag)

                        Log.d("tagList", tagList.toString())
                    }
                    val tagListSize = tagList.size
                    Log.d("tagListSize", tagListSize.toString()) //log size
                    val randomTagList: MutableList<HashTags?> = ArrayList()
                    for (i in 0 until tagListSize) {
                        val randomTag: HashTags? = tagList[Random().nextInt(tagListSize)]
                        if (!randomTagList.contains(randomTag)) {
                            randomTagList.add(randomTag)
                            if (randomTagList.size == 10) {
                                break
                            }
                        }
                        binding.etInputView.text.clear()
                        val ic: InputConnection = binding.etInputView.onCreateInputConnection(EditorInfo())
                        ic.commitText(randomTagList.toString(), 1)
                        Log.d("ClickKeyData", randomTagList.toString())
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }*/


    /* override fun onItemClick(position: Int) {
         val list = listOf("one", "two", "three", "four", "five")
         val randomElements = list.asSequence().shuffled().take(progressCount).toList()

         val clickedItem: ButtonGroup = buttonList[position]
         val clickedButtonId = clickedItem.id
         var tagList = ""

 //        val tagList: MutableList<HashTags> = mutableListOf()
 //        val tagList: MutableList<String> = mutableListOf()

         mFirestoreDb.collection("hashtags")
             .whereEqualTo("buttonId", clickedButtonId)
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     task.result.forEach { document ->
                         val tag = document.toObject(HashTags::class.java)
                         tag.buttonId = document.id

 //                        tagList += (tag.tagName!!).asSequence().shuffled().take(progressCount).toString()
                         tagList += listOf(tag.tagName).toString().asSequence().shuffled().take(progressCount).toList().toString()
                     }

                     binding.etInputView.text.clear()
                     val ic: InputConnection =
                         binding.etInputView.onCreateInputConnection(EditorInfo())
                     ic.commitText(tagList.toString(), 1)
                     Log.d("ClickKeyData", tagList.toString())
                 }
             }

         *//*binding.etInputView.text.clear()
        val ic: InputConnection = binding.etInputView.onCreateInputConnection(EditorInfo())
//        ic.commitText(randomElements.toString(), 1)
        ic.commitText(data, 1)
        Log.d("ClickKeyData", randomElements.toString())*//*

    }*/


}