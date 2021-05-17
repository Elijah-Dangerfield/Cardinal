package com.dangerfield.cardinal.presentation.util

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.dangerfield.cardinal.domain.model.Article
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.visibleIf(predicate: Boolean) {
    this.visibility = if(predicate) View.VISIBLE else View.GONE
}

fun ConstraintLayout.cloneSet(): ConstraintSet {
    val set = ConstraintSet()
    set.clone(this)
    return set
}

fun RecyclerView.addOnScrolledListener(listener: () -> Unit) {
    this.addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if(recyclerView.scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                listener.invoke()
            }
        }
    })
}

private val keyboardHider = View.OnFocusChangeListener { view, b ->
    if (!b) { hideKeyboardFrom(view) }
}

private fun hideKeyboardFrom(view: View) {
    val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText.closeKeyboard() {
    this.hideKeyboard()
    this.clearFocus()
    this.isCursorVisible = false
}

fun View.goneIf(condition: Boolean) {
    this.visibility = if(condition) View.GONE else View.VISIBLE
}

fun EditText.addOnFocusedListener(listener: () -> Unit) {
    this.onFocusChangeListener = View.OnFocusChangeListener { v, focused -> if(focused) listener() }
}

fun EditText.onSearchPressed(listener: (s: String) -> Unit) {
    this.setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(view: TextView?, actionID: Int, event: KeyEvent?): Boolean {
            if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                listener.invoke(this@onSearchPressed.text.toString().trim())
                return true
            }
            return false
        }
    })
}

fun EditText.onDonePressed(listener: (s: String) -> Unit) {
    this.setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(view: TextView?, actionID: Int, event: KeyEvent?): Boolean {
            if (actionID == EditorInfo.IME_ACTION_DONE) {
                listener.invoke(this@onDonePressed.text.toString().trim())
                return true
            }
            return false
        }
    })
}

fun EditText.openKeyboard() {
    val imm = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    this.requestFocus()
    this.isCursorVisible = true
    this.isFocusableInTouchMode = true;
    this.isFocusable = true
}

fun EditText.onTextChanged(action: ((text: String) -> Unit)){
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            action(p0.toString())
        }
    })
}

/**
 * converts time stamp given by api to a readable format
 */
fun Article.getReadableDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    var date: Date? = null
    try {
        date = formatter.parse(this.publishedAt)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date.toString().dropLast(18)
}

