package com.dicoding.storyapp.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}