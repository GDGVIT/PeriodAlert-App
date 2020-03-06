package com.dscvit.periodsapp.ui.auth


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.firebase.AuthHelper
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.model.signup.SignupRequest
import com.dscvit.periodsapp.model.signup.SignupResponse
import com.dscvit.periodsapp.network.PreAuthApiService
import com.dscvit.periodsapp.ui.PostAuthActivity
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.dscvit.periodsapp.utils.PreferenceHelper.get
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class DetailsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsProgressBar.hide()

        val authViewModel by sharedViewModel<AuthViewModel>()

        sharedPreferences = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        requireActivity().onBackPressedDispatcher.addCallback(this) {}

        finishButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val phoneNumber: String = sharedPreferences[Constants.PREF_PHONE_NUMBER] ?: ""

            if (isEmailValid(email)) {
                if (password == confirmPassword) {
                    if (name.isNotEmpty() && password.isNotEmpty()) {
                        val signUpRequest = SignupRequest(
                            email = email,
                            username = name,
                            password = password,
                            phoneNo = phoneNumber
                        )

                        authViewModel.signUpUser(signUpRequest)
                            .observe(viewLifecycleOwner, Observer {
                                when (it.status) {
                                    Result.Status.LOADING -> {
                                        detailsProgressBar.show()

                                        finishButton.hide()
                                        emailEditText.disable()
                                        nameEditText.disable()
                                        passwordEditText.disable()
                                        confirmPasswordEditText.disable()
                                    }
                                    Result.Status.SUCCESS -> {
                                        if (it.data?.message == "User Signed up successfully") {
                                            sharedPreferences[Constants.PREF_IS_LOGGED_IN] = true
                                            sharedPreferences[Constants.PREF_AUTH_KEY] =
                                                it.data.user.token
                                            sharedPreferences[Constants.PREF_USER_ID] =
                                                it.data.user.id

                                            detailsProgressBar.hide()

                                            val intent =
                                                Intent(
                                                    requireContext(),
                                                    PostAuthActivity::class.java
                                                )
                                            startActivity(intent)
                                            requireActivity().finish()
                                        }
                                    }
                                    Result.Status.ERROR -> {
                                        if (it.message == "400 Bad Request") {
                                            requireContext().shortToast("User Exist, Try Signing In")
                                        } else {
                                            requireContext().shortToast("No Internet")
                                        }

                                        finishButton.show()
                                        emailEditText.enable()
                                        nameEditText.enable()
                                        passwordEditText.enable()
                                        confirmPasswordEditText.enable()

                                        detailsProgressBar.hide()
                                        Log.d("esh", it.message)
                                    }
                                }
                            })
                    } else {
                        requireContext().shortToast("Fields cannot be empty")
                    }
                } else {
                    requireContext().shortToast("Passwords don't match")
                }
            } else {
                requireContext().shortToast("Enter a valid Email")
            }
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
