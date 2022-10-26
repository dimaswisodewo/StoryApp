package com.dicoding.storyapp.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.view.main.MainActivity
import com.dicoding.storyapp.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        supportActionBar?.hide()

        subscribe()
        init()
    }

    private fun init() {
        with (binding) {
            // Set edit text name visibility
            setNameEditTextVisibility(tvSwitchToRegister.isSwitchedToRegister)

            // Switch to register
            val ss = SpannableString(tvSwitchToRegister.text)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(v: View) {

                }
            }
            tvSwitchToRegister.onItemClickCallback = object : LoginRegisterToggleText.OnItemClickCallback {
                override fun onItemClick() {
                    super.onItemClick()
                    btnLogin.setText(
                        if (tvSwitchToRegister.isSwitchedToRegister) R.string.register
                        else R.string.login
                    )
                    setNameEditTextVisibility(tvSwitchToRegister.isSwitchedToRegister)
                }
            }

            // Login & Register button
            btnLogin.setOnClickListener {
                if (isFormValid()) {
                    val emailValue = etEmail.text.toString()
                    val passwordValue = etPassword.text.toString()
                    // Login or Register
                    if (tvSwitchToRegister.isSwitchedToRegister) {
                        val nameValue = etName.text.toString()
                        loginViewModel.register(nameValue, emailValue, passwordValue)
                        Log.d(LoginActivity::class.java.simpleName, "Register, name: ${nameValue}, email: ${emailValue}, password: ${passwordValue}")
                    }
                    else {
                        loginViewModel.login(emailValue, passwordValue)
                        Log.d(LoginActivity::class.java.simpleName, "Login, email: ${emailValue}, password: ${passwordValue}")
                    }
                } else {
                    Log.d(LoginActivity::class.java.simpleName, "Form is not valid")
                }
            }

            // Switch button
            btnSwitchShowPassword.setOnClickListener {
                if (btnSwitchShowPassword.isChecked) {
                    tvShowPassword.setText(R.string.hide_password)
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    tvShowPassword.setText(R.string.show_password)
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
    }

    private fun subscribe() {
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.isError.observe(this) {
            if (it) showError()
        }
        loginViewModel.loginResult.observe(this) {
            val toMainActivity = Intent(this, MainActivity::class.java)
            startActivity(toMainActivity)
        }
    }

    private fun showLoading(isActive: Boolean) {
        with (binding) {
            if (isActive) {
                btnLogin.isClickable = false
                tvSwitchToRegister.isClickable = false
                progressBar.visibility = View.VISIBLE
            } else {
                btnLogin.isClickable = true
                tvSwitchToRegister.isClickable = true
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showError() {
        loginViewModel.isSnackBarShown.getContentIfNotHandled()?.let {
            Snackbar.make(binding.root, loginViewModel.errorMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setNameEditTextVisibility(isVisible: Boolean) {
        binding.etName.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun isFormValid() : Boolean {
        with (binding) {
            if (etName.isVisible) {
                if (etName.text!!.isEmpty()) {
                    etName.error = getString(R.string.field_error_name)
                    return false
                }
            }

            if (etEmail.text!!.isEmpty()) {
                etEmail.error = getString(R.string.field_error_email)
                return false
            }

            if (etPassword.text!!.isEmpty()) {
                etPassword.error = getString(R.string.field_error_password)
                return false
            } else if (etPassword.text!!.length < 8) {
                etPassword.error = getString(R.string.field_error_password_length)
                return false
            }

            return true
        }
    }
}