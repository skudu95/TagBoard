package com.kudu.tagboard.activities

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.tagboard.adapter.ButtonGroupListViewAdapter
import com.kudu.tagboard.databinding.ActivitySettingsBinding
import com.kudu.tagboard.model.ButtonGroup

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var buttonListAdapter: ButtonGroupListViewAdapter
    private val buttonList: ArrayList<ButtonGroup> = ArrayList()

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
                    getButtonListFromSeekbar(progress)
                    buttonListAdapter.notifyDataSetChanged()
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

    private fun getButtonListFromSeekbar(count: Int) {
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
        buttonListAdapter = ButtonGroupListViewAdapter(this, buttonList)
        binding.buttonsRv.adapter = buttonListAdapter
    }
}