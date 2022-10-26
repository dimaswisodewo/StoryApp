package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.model.GeneralResponse
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.LoginResult
import com.dicoding.storyapp.model.User
import com.dicoding.storyapp.networking.ApiConfig
import com.dicoding.storyapp.other.Event
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    val gson = Gson()

    var errorMessage: String = ""
        private set

    var isSnackBarShown: Event<Boolean> = Event(false)

    fun login(email: String, password: String) {
        isSnackBarShown.hasBeenHandled = false
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _isLoading.value = false
                    if (responseBody.error!!) {
                        val errorResponse = gson.fromJson(
                            response.errorBody()!!.string(),
                            GeneralResponse::class.java
                        )
                        onError(errorResponse.message)
                    } else {
                        // Login success
                        with (responseBody) {
                            with (ApiConfig.USER) {
                                id = loginResult?.userId.toString()
                                name = loginResult?.name.toString()
                                token = loginResult?.token.toString()
                            }
                        }
                        _loginResult.postValue(responseBody.loginResult!!)
                    }
                } else {
                    _isLoading.value = false
                    val errorResponse = gson.fromJson(
                        response.errorBody()!!.string(),
                        GeneralResponse::class.java
                    )
                    onError(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    fun register(name: String, email: String, password: String) {
        isSnackBarShown.hasBeenHandled = false
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _isLoading.value = false
                    if (responseBody.error!!) {
                        val errorResponse = gson.fromJson(
                            response.errorBody()!!.string(),
                            GeneralResponse::class.java
                        )
                        onError(errorResponse.message)
                    } else {
                        Log.d("Register", "Register success")
                    }
                } else {
                    _isLoading.value = false
                    val errorResponse = gson.fromJson(
                        response.errorBody()!!.string(),
                        GeneralResponse::class.java
                    )
                    onError(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun onError(inputMessage: String?) {
        var message = inputMessage
        message = if (message.isNullOrBlank() or message.isNullOrEmpty()) ApiConfig.UNKNOWN_ERROR
        else message

        errorMessage = StringBuilder("ERROR: ").append(message).toString()

        _isError.value = true
    }
}