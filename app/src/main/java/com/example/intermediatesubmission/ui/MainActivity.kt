package com.example.intermediatesubmission.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.intermediatesubmission.databinding.ActivityMainBinding
import com.example.intermediatesubmission.ui.auth.AuthActivity
import com.example.intermediatesubmission.ui.home.HomeActivity
import com.example.intermediatesubmission.util.SharedPreferences

// Main activity for the app
// Direct user to either:
//  - Login page if user is not logged in
//  - Home page if user is logged in
// Logged in check is done by checking if the user token is present in the shared preferences
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPref = SharedPreferences(this)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onStart() {
        super.onStart()
        lateinit var intent: Intent
        // Check if user is logged in
        // If user is logged in, direct user to home page
        // If user is not logged in, direct user to login page
        intent = if (sharedPref?.getToken() != null) Intent(this, HomeActivity::class.java)
        else Intent(this, AuthActivity::class.java)

        Log.d("MainActivity", "User is logged in: ${sharedPref?.getToken() != null}")
        startActivity(intent)
    }
}