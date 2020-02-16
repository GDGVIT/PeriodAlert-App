package com.dscvit.periodsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import kotlinx.android.synthetic.main.activity_post_auth.*
import java.util.*

class PostAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_auth)
        setUpNavigation()

        val sharedPreferences = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)

        if(sharedPreferences.getString(Constants.PREF_DEVICE_ID, null) == null) {
            val deviceId: String = UUID.randomUUID().toString()
            sharedPreferences[Constants.PREF_DEVICE_ID] = deviceId
            Log.d("esh", "Device ID: $deviceId")
        }

        onBackPressedDispatcher.addCallback(this) {}
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(bottomNavigation, post_auth_nav_host.findNavController())
    }
}
