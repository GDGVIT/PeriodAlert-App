package com.dscvit.periodsapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper

class PreAuthActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_auth)

        sharedPreferences = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
    }

    override fun onStart() {
        super.onStart()

        val isLoggedIn = sharedPreferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false)

        if (isLoggedIn) {
            val intent = Intent(this, PostAuthActivity::class.java)
            startActivity(intent)
        }
    }
}
