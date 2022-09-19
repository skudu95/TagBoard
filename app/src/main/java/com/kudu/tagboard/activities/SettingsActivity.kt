package com.kudu.tagboard.activities

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

    override fun onItemClick(position: Int) {

        val clickedItem: ButtonGroup = buttonList[position]
        val clickedButtonId = clickedItem.id

        val mFirestoreDb = FirebaseFirestore.getInstance()
        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", clickedButtonId)
            .limit(progressCount.toLong()) //TODO: make changes here
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
                val ic: InputConnection = binding.etInputView.onCreateInputConnection(EditorInfo())
                ic.commitText(data, 1)
                Log.d("ClickKeyData", data)
            }
    }
}