package com.dscvit.periodsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dscvit.periodsapp.R
import com.google.firebase.auth.FirebaseAuth

class PreAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_auth)
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser

        if(user != null) {
            val intent = Intent(this, PostAuthActivity::class.java)
            startActivity(intent)
        }
    }
}
