package com.dicoding.storyapp.view.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.localdata.UserPreferences

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOGIN_RESULT = "extra_login_result"
    }

    private lateinit var binding: ActivityMainBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPreferences")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        // TODO: create MainViewModel then create functions to return user preference as LiveData
//        binding.tvWelcome.text = "Welcome ${pref.getUserName()}!"

        supportActionBar?.title = getString(R.string.stories)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun subscribe() {
        binding.rvListStory.layoutManager = LinearLayoutManager(this)

    }
}