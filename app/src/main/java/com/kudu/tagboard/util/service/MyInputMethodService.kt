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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.tagboard.R
import com.kudu.tagboard.activities.GroupsActivity
import com.kudu.tagboard.adapter.KeyboardViewAdapter
import com.kudu.tagboard.model.ButtonGroup
import com.kudu.tagboard.model.HashTags


@Suppress("DEPRECATION")
class MyInputMethodService : InputMethodService(), OnKeyboardActionListener,
    KeyboardViewAdapter.OnItemClickListener {

    private val buttonList: ArrayList<ButtonGroup> = ArrayList()
//    private var myButtonId: String = HashTagActivity().mButtonId

//    val ic = currentInputConnection

    override fun onCreateInputView(): View {

        val myView = View.inflate(this, R.layout.keyboard_view_layout, null) as RelativeLayout
        val keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        val keyboard = Keyboard(this, R.xml.test_keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)

        //working with the buttons inside
        val ic = currentInputConnection
        val backspace = myView.findViewById<ImageButton>(R.id.btn_backspace)
        val btnSettings = myView.findViewById<ImageButton>(R.id.settings_to_app)
        val btnKeyboardPicker = myView.findViewById<ImageButton>(R.id.keyboard_picker)

        //keyboard picker button
        btnKeyboardPicker.setOnClickListener {
            val imeManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            if (imeManager != null) {
                imeManager.showInputMethodPicker()
            }
        }

        //settings button to redirect to app UI
        btnSettings.setOnClickListener {
            val intent = Intent(this, GroupsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        //backspace button
        backspace.setOnClickListener {
//            Toast.makeText(this, "Backspace clicked", Toast.LENGTH_SHORT).show()
            val selectedText = ic.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                ic.deleteSurroundingText(1, 0)
            } else {
                // delete the selection
                ic.commitText("", 1)
            }
        }

        //get the list from firestore
        val mFirestoreDb = FirebaseFirestore.getInstance()

        mFirestoreDb.collection("buttons")
            .get()
            .addOnSuccessListener { document ->
                Log.e("ButtonList Keyboard", document.documents.toString())
                for (i in document.documents) {
                    val buttonGroup = i.toObject(ButtonGroup::class.java)
                    buttonGroup!!.id = i.id
                    buttonList.add(buttonGroup)
                }
                val buttonKeyboardRV = myView.findViewById<RecyclerView>(R.id.buttons_keyboard_rv)
                buttonKeyboardRV.setItemViewCacheSize(8)
                buttonKeyboardRV.layoutManager = GridLayoutManager(this, 2)
                val buttonAdapter = KeyboardViewAdapter(this, buttonList, this)
                buttonKeyboardRV.adapter = buttonAdapter
                buttonAdapter.notifyDataSetChanged()
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

    override fun onItemClick(position: Int) {
//        Toast.makeText(this, "item $position", Toast.LENGTH_SHORT).show()
        val clickedItem: ButtonGroup = buttonList[position]
        val clickedButtonId = clickedItem.id

        val mFirestoreDb = FirebaseFirestore.getInstance()
        mFirestoreDb.collection("hashtags")
            .whereEqualTo("buttonId", clickedButtonId)
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