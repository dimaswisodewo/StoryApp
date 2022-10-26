package com.dicoding.storyapp.view.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.dicoding.storyapp.R

class LoginRegisterToggleText : AppCompatTextView {

    var isSwitchedToRegister: Boolean = false
    var onItemClickCallback: OnItemClickCallback? = null

    private lateinit var spannableString: SpannableString
    private lateinit var clickableSpan: ClickableSpan

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        // Make text view clickable
        clickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                isSwitchedToRegister = !isSwitchedToRegister

                // Update text
                spannableString = SpannableString(
                    if (isSwitchedToRegister) context.getString(R.string.already_have_account)
                    else context.getString(R.string.i_dont_have_account)
                )
                spannableString.setSpan(clickableSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                text = spannableString

                onItemClickCallback?.onItemClick()
            }
        }
        spannableString = SpannableString(
            if (isSwitchedToRegister) context.getString(R.string.already_have_account)
            else context.getString(R.string.i_dont_have_account)
        )
        spannableString.setSpan(clickableSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    interface OnItemClickCallback {
        fun onItemClick() {
//            Log.d(LoginRegisterToggleText::class.java.simpleName, "Clicked on LoginRegisterToggleText")
        }
    }
}