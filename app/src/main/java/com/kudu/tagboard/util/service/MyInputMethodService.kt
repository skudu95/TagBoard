package com.kudu.tagboard.util.service


import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.tagboard.R
import com.kudu.tagboard.adapter.KeyboardViewAdapter
import com.kudu.tagboard.model.ButtonGroup


@Suppress("DEPRECATION")
class MyInputMethodService : InputMethodService(), OnKeyboardActionListener {

    val buttonList: ArrayList<ButtonGroup> = ArrayList()

    override fun onCreateInputView(): View {
/*        // get the KeyboardView and add our Keyboard layout to it
//        val keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView

        *//* val KeyboardLayout: RelativeLayout? =
             getLayoutInflater().inflate(R.layout.keyboard_layout, null) as RelativeLayout?*//*

//        val keyboardView: LinearLayout? = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout?
        // 1  val keyboard = Keyboard(this, R.xml.hashtag_keyboard)*/

        val myView = View.inflate(this, R.layout.keyboard_view_layout, null) as RelativeLayout
        val keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
//        val keyboardView = myView.findViewById<KeyboardView>(R.id.keyboard_view_under_relative)
        val keyboard = Keyboard(this, R.xml.test_keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)

        //working with the buttons inside
        val ic = currentInputConnection
        val backspace = myView.findViewById<ImageButton>(R.id.btn_backspace)

        backspace.setOnClickListener {
            Toast.makeText(this, "Backspace clicked", Toast.LENGTH_SHORT).show()
//            val ic = currentInputConnection
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
                val buttonAdapter = KeyboardViewAdapter(this, buttonList)
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
        val buttonAdapter = KeyboardViewAdapter(this, buttonList)
        buttonKeyboardRV.adapter = buttonAdapter
        buttonAdapter.notifyDataSetChanged()
        /*val buttonKeyboardRV = myView.findViewById<RecyclerView>(R.id.buttons_keyboard_rv)
        buttonKeyboardRV.setItemViewCacheSize(8)
        val templist = ArrayList<String>()
        templist.add("Button 1")
        templist.add("Button 2")
        templist.add("Button 3")
        templist.add("Button 4")
        templist.add("Button 5")
        templist.add("Button 6")
        templist.add("Button 7")
        templist.add("Button 8")
        templist.add("Button 9")
        templist.add("Button 10")
        templist.add("Button 11")
        templist.add("Button 12")
        templist.add("Button 13")
        templist.add("Button 14")
        templist.add("Button 15")
        templist.add("Button 16")
        templist.add("Button 17")
        templist.add("Button 18")
        templist.add("Button 19")
        templist.add("Button 20")
        templist.add("Button 21")
        templist.add("Button 22")



        buttonKeyboardRV.layoutManager = GridLayoutManager(this, 2)
//        var groupList: ButtonGroupList = ButtonGroupList()
//        val adapter = GroupListAdapter(this, groupList = GroupsActivity.groupList.ref)
//        val adapter = KeyboardButtonListAdapter(this, templist)
        val buttonAdapter = KeyboardViewAdapter(this, buttonList)
//        buttonKeyboardRV.adapter = adapter
        buttonKeyboardRV.adapter = buttonAdapter*/

        /*val button1 = myView.findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            Toast.makeText(this, "Button1 clicked", Toast.LENGTH_SHORT).show()
            ic.commitText("Hello", 1)
        }*/

        myView.addView(keyboardView)
        return myView

        /*keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView*/
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

    /*   //work with buttons inside keyboard
       private fun getButtonsWorking() {
           val myView = View.inflate(this, R.layout.keyboard_view_layout, null) as RelativeLayout
           val ic = currentInputConnection ?: return

           //working with the buttons inside
           val backspace = myView.findViewById<ImageButton>(R.id.btn_backspace)

           backspace.setOnClickListener {
               Toast.makeText(this, "Backspace clicked", Toast.LENGTH_SHORT).show()
   //            val ic = currentInputConnection
               val selectedText = ic.getSelectedText(0)
               if (TextUtils.isEmpty(selectedText)) {
                   // no selection, so delete previous character
                   ic.deleteSurroundingText(1, 0)
               } else {
                   // delete the selection
                   ic.commitText("", 1)
               }
           }

           val button1 = myView.findViewById<Button>(R.id.button1)
           button1.setOnClickListener {
               Toast.makeText(this, "Button1 clicked", Toast.LENGTH_SHORT).show()
               ic.commitText("Hello", 1)
           }
       }*/

    //get list of buttons from firestore
    private fun getButtonList() {
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
                initialiseAdapter()
            }
            .addOnFailureListener {
                Log.e("ButtonListErrorKeyboard", "Error querying button list")
            }
    }

    //initialise adapter
    private fun initialiseAdapter() {
        val myView = View.inflate(this, R.layout.keyboard_view_layout, null) as RelativeLayout
        val buttonKeyboardRV = myView.findViewById<RecyclerView>(R.id.buttons_keyboard_rv)
        buttonKeyboardRV.setItemViewCacheSize(8)
        buttonKeyboardRV.layoutManager = GridLayoutManager(this, 2)
        val buttonAdapter = KeyboardViewAdapter(this, buttonList)
        buttonKeyboardRV.adapter = buttonAdapter
        buttonAdapter.notifyDataSetChanged()
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