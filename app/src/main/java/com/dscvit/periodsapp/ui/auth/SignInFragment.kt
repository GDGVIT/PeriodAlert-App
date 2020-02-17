package com.dscvit.periodsapp.ui.auth


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.model.login.LoginRequest
import com.dscvit.periodsapp.network.PreAuthApiService
import com.dscvit.periodsapp.ui.PostAuthActivity
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.dscvit.periodsapp.utils.shortToast
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.Exception


class SignInFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        val authViewModel by sharedViewModel<AuthViewModel>()

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val loginRequest = LoginRequest(email = email, password = password, username = null)

            authViewModel.signInUser(loginRequest).observe(viewLifecycleOwner, Observer {
                when(it.status) {
                    Result.Status.LOADING -> {
                        requireContext().shortToast("Loading")
                    }
                    Result.Status.SUCCESS -> {
                        if(it.data?.message == "User Logged In") {
                            sharedPreferences[Constants.PREF_IS_LOGGED_IN] = true
                            sharedPreferences[Constants.PREF_AUTH_KEY] = it.data.user.token

                            val intent = Intent(requireContext(), PostAuthActivity::class.java)
                            startActivity(intent)
                        } else {
                            requireContext().shortToast("Email or password is wrong")
                        }
                    }
                    Result.Status.ERROR -> {
                        Log.d("esh", it.message)
                    }
                }
            })
        }
    }
}
