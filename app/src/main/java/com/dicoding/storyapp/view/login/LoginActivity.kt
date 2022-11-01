package com.dicoding.storyapp.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.localdata.UserPreferences
import com.dicoding.storyapp.model.LoginResult
import com.dicoding.storyapp.view.main.MainActivity
import com.dicoding.storyapp.viewmodel.LoginViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPreferences")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        loginViewModel = ViewModelProvider(this, ViewModelFactory(application, pref)).get(LoginViewModel::class.java)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        supportActionBar?.hide()

        subscribe()

        loginViewModel.isUserDataExists().observe(this) {
            if (it) {
                lifecycleScope.launch {
                    val loginResult = LoginResult(
                        name = loginViewModel.getUserDataUsername().first(),
                        userId = loginViewModel.getUserDataId().first(),
                        token = loginViewModel.getUserDataToken().first()
                    )

                    Log.d("LoginActivity", "Saved data exists, name: ${loginResult.name}," +
                            " id: ${loginResult.userId}, token: ${loginResult.token}")

                    val toMainActivityIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    toMainActivityIntent.putExtra(MainActivity.EXTRA_LOGIN_RESULT, loginResult)
                    startActivity(toMainActivityIntent)
                }
            } else {
                init()
            }
        }
    }

    private fun init() {
        with (binding) {
            // Set edit text name visibility
            setNameEditTextVisibility(tvSwitchToRegister.isSwitchedToRegister)

            // Switch to register
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
                        Log.d(LoginActivity::class.java.simpleName, "Register, name: $nameValue, email: $emailValue, password: $passwordValue")
                    }
                    else {
                        loginViewModel.login(emailValue, passwordValue)
                        Log.d(LoginActivity::class.java.simpleName, "Login, email: $emailValue, password: $passwordValue")
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
            toMainActivity.putExtra(MainActivity.EXTRA_LOGIN_RESULT, it)
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
            } else {
                val etEmailValue = etEmail.text as CharSequence
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmailValue).matches()) {
                    etEmail.error = getString(R.string.field_error_email_pattern)
                    return false
                }
            }

            if (etPassword.text!!.isEmpty()) {
                etPassword.error = getString(R.string.field_error_password)
                return false
            } else if (etPassword.text!!.length < 6) {
                etPassword.error = getString(R.string.field_error_password_length)
                return false
            }

            return true
        }
    }
}