package com.dicoding.storyapp.view.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R

class LoginButton : AppCompatButton, View.OnClickListener {

    private lateinit var btnBackground: Drawable
    private lateinit var btnBackgroundDisabled: Drawable
    private var isSwitchedToRegister: Boolean = false

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
        btnBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        btnBackgroundDisabled = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled) as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) btnBackground else btnBackgroundDisabled
        text = if (isSwitchedToRegister) R.string.register.toString() else R.string.login.toString()
    }

    override fun onClick(v: View?) {
        if (isSwitchedToRegister) {
            // Register
        } else {
            // Login
        }
    }
}