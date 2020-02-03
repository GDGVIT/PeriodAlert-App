package com.dscvit.periodsapp.firebase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.navigation.findNavController
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.ui.auth.SignUpFragmentDirections
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class AuthHelper(val context: Context, val view: View, private val activity: Activity) {

    // variables for setting up the shared preferences
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "app-pref"

    val sharedPref: SharedPreferences = activity.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    var verificationId = ""
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun verificationCallbacks() {
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(verification: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verification, p1)
                verificationId = verification

                sharedPref.edit {
                    putString("VERID", verificationId)
                    commit()
                }

                val action = SignUpFragmentDirections.actionSignUpFragmentToOtpVerificationFragment()
                view.findNavController().navigate(action)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signIn(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Invalid Credential", Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(context, "Too Many Requests", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun sendOtp(numberEditText: EditText) {
        verificationCallbacks()

        val phoneNumber = "+91" + numberEditText.text.toString()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity,
            mCallbacks
        )
    }

    private fun signIn(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()

                    view.findNavController().navigate(R.id.detailsFragment)
                } else {
                    Toast.makeText(context, "Wrong OTP", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun authenticate(otpEditText: EditText) {
        val verNo = otpEditText.text.toString()
        val verId = sharedPref.getString("VERID", "")
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verId!!, verNo)
        signIn(credential)
    }

}