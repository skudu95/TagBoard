package com.kudu.tagboard.util.service


import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kudu.tagboard.R
import com.kudu.tagboard.activities.GroupsActivity
import com.kudu.tagboard.adapter.KeyboardViewAdapter
import com.kudu.tagboard.model.ButtonGroup
import com.kudu.tagboard.model.HashTags


@Suppress("DEPRECATION")
class MyInputMethodService : InputMethodService(), OnKeyboardActionListener,
    KeyboardViewAdapter.OnItemClickListener {

    private val buttonList: ArrayList<ButtonGroup> = ArrayList()
    private val mFirestoreDb = FirebaseFirestore.getInstance()
    private val buttonAdapter = KeyboardViewAdapter(this, buttonList, this)
    var progressCount = 0

//    val ic = currentInputConnection

    override fun onCreateInputView(): View {

        val myView = View.inflate(this, R.layout.keyboard_view_layout, null) as RelativeLayout
        val keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        val keyboard = Keyboard(this, R.xml.test_keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)

        //working with the buttons inside
//        val ic = currentInputConnection
        val backspace = myView.findViewById<ImageButton>(R.id.btn_backspace)
        val enter = myView.findViewById<ImageButton>(R.id.btn_enter_next_line)
        val btnSettings = myView.findViewById<ImageButton>(R.id.settings_to_app)
        val btnKeyboardPicker = myView.findViewById<ImageButton>(R.id.keyboard_picker)
        val seekBar = myView.findViewById<SeekBar>(R.id.tag_seekbar)
        val seekBarText = myView.findViewById<TextView>(R.id.tag_seekbar_text)

        //keyboard picker button
        btnKeyboardPicker.setOnClickListener {
            val imeManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            if (imeManager != null) {
                imeManager.showInputMethodPicker()
            }
        }

        //seekbar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                val strProgress = progress.toString()

                if (fromUser) {
                    seekBarText.text = "$strProgress Tags"
                    progressCount = progress
                }

                /*  if (fromUser) {
                      seekBarText.text = "$strProgress Tags"
                      mFirestoreDb.collection("buttons")
  //                    .whereEqualTo("tagItemsNumber", progress)
                          .limit(progress.toLong())
                          .get()
                          .addOnCompleteListener { task ->
                              if (task.isSuccessful) {
                                  if (buttonList != null) {
                                      buttonList.clear()
                                  }

                                  for (document in task.result) {
                                      val buttonGroup = document.toObject(ButtonGroup::class.java)
                                      buttonGroup.id = document.id
                                      buttonList.add(buttonGroup)
                                  }
  //                            initialiseAdapter()
                                  progressCount = progress
                                  buttonAdapter.notifyDataSetChanged()
                              }
                          }
                          .addOnFailureListener {
                              Log.e("ButtonListError", "Error querying button list")

                          }
                  }*/
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekbar: SeekBar?) = Unit

        })

        //settings button to redirect to app UI
        btnSettings.setOnClickListener {
            val intent = Intent(this, GroupsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //backspace button
        backspace.setOnClickListener {
//            Toast.makeText(this, "Backspace clicked", Toast.LENGTH_SHORT).show()

            val ic = currentInputConnection
            val selectedText = ic.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                ic.deleteSurroundingText(1, 0)
            } else {
                // delete the selection
                ic.commitText("", 1)
            }
        }

        //long press backspace
        backspace.setOnLongClickListener {
            val ic = currentInputConnection

            val lengthToDelete: Int = checkForLengthToDelete()
            ic.deleteSurroundingText(lengthToDelete, 0)
        }

        //enter button
        enter.setOnClickListener {
            val ic = currentInputConnection

            ic.commitText("\n", 1)
        }

        //get the list from firestore
//        val mFirestoreDb = FirebaseFirestore.getInstance()
        mFirestoreDb.collection("buttons")
            .orderBy("creationTimeButton", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (buttonList != null) {
                        buttonList.clear()
                    }
                    for (document in task.result) {
                        val buttonGroup = document.toObject(ButtonGroup::class.java)
                        buttonGroup.id = document.id
                        buttonList.add(buttonGroup)
                    }
                    val buttonKeyboardRV =
                        myView.findViewById<RecyclerView>(R.id.buttons_keyboard_rv)
                    buttonKeyboardRV.setItemViewCacheSize(8)
                    buttonKeyboardRV.layoutManager = GridLayoutManager(this, 2)
//                    val buttonAdapter = KeyboardViewAdapter(this, buttonList, this)
                    buttonKeyboardRV.adapter = buttonAdapter
                    buttonAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                Log.e("ButtonListErrorKeyboard", "Error querying button list")
            }

        // adapter initialise
        val buttonKeyboardRV = myView.findViewById<RecyclerView>(R.id.buttons_keyboard_rv)
        buttonKeyboardRV.setItemViewCacheSize(8)
        buttonKeyboardRV.layoutManager = GridLayoutManager(this, 2)
        val buttonAdapter = KeyboardViewAdapter(this, buttonList, this)
        buttonKeyboardRV.adapter = buttonAdapter
        buttonAdapter.notifyDataSetChanged()


        myView.addView(keyboardView)
        return myView
    }

    @Deprecated("Deprecated in Java")
    override fun onKey(primaryCode: Int, keyCodes: IntArray) {
        val ic = currentInputConnection ?: return
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                val selectedText = ic.getSelectedText(0)
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1, 0)
                } else {
                    // delete the selection
                    ic.commitText("", 1)
                }
            }
            else -> {
                val code = primaryCode.toChar()
                ic.commitText(code.toString(), 1)
            }
        }
    }

    private fun checkForLengthToDelete(): Int {
        val ic = currentInputConnection
        // Returns the length of characters for deletion, until the previous
        // hashtag symbol:
        val text: String = ic.getTextBeforeCursor(100, 0).toString()
        if (text.isNotEmpty()) {
            val textLength = text.length
            val lastOccurrenceOfHashtag = text.lastIndexOf('#')
            return textLength - lastOccurrenceOfHashtag
        }
        return 0
    }

   /* override fun onItemClick(position: Int) {
//        Toast.makeText(this, "item $position", Toast.LENGTH_SHORT).show()
        val clickedItem: ButtonGroup = buttonList[position]
        val clickedButtonId = clickedItem.id

        val mFirestoreDb = FirebaseFirestore.getInstance()
        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", clickedButtonId)
            .limit(progressCount.toLong())
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
                val ic = currentInputConnection
                ic.commitText(data, 1)
                Log.d("ClickKeyData", data)
            }
    }*/

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
                        shuffledTagList.toString().replace("[", "").replace("]", " ")
                            .replace(",", "")

                    val tagListSize = tagList.size
                    Log.d("tagListSize", tagListSize.toString())

                    val ic = currentInputConnection
                    ic.commitText(noCommaBracket, 1)
                    Log.d("ClickKeyData", noCommaBracket)
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onPress(primaryCode: Int) {
    }

    @Deprecated("Deprecated in Java")
    override fun onRelease(primaryCode: Int) {
    }

    @Deprecated("Deprecated in Java")
    override fun onText(text: CharSequence) {
    }

    @Deprecated("Deprecated in Java")
    override fun swipeLeft() {
    }

    @Deprecated("Deprecated in Java")
    override fun swipeRight() {
    }

    @Deprecated("Deprecated in Java")
    override fun swipeDown() {
    }

    @Deprecated("Deprecated in Java")
    override fun swipeUp() {
    }


}