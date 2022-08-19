package com.kudu.tagboard.util.service


import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import com.kudu.tagboard.R


@Suppress("DEPRECATION")
class MyInputMethodService : InputMethodService(), OnKeyboardActionListener {
    override fun onCreateInputView(): View {
        // get the KeyboardView and add our Keyboard layout to it
//        val keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView

        /* val KeyboardLayout: RelativeLayout? =
             getLayoutInflater().inflate(R.layout.keyboard_layout, null) as RelativeLayout?*/

//        val keyboardView: LinearLayout? = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout?
        // 1  val keyboard = Keyboard(this, R.xml.hashtag_keyboard)


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

        val button1 = myView.findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            Toast.makeText(this, "Button1 clicked", Toast.LENGTH_SHORT).show()
            ic.commitText("Hello", 1)
        }


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

    //work with buttons inside keyboard
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