package com.dscvit.periodsapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.firebase.AuthHelper
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        // Gets the phone number and sends otp using firebase
        sendOtpButton.setOnClickListener {
            if (phoneNumberEditText.text.length == 10) {
                sendOtpButton.disable()
                sendOtpButton.hide()

                val authHelper = AuthHelper(requireContext(), view, requireActivity())
                authHelper.sendOtp(phoneNumberEditText)

                sharedPreferences[Constants.PREF_PHONE_NUMBER] = phoneNumberEditText.text.toString()
            } else {
                requireContext().shortToast("Enter a valid phone number")
            }
        }

        signInInsteadButton.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.pre_auth_nav_host)
            navController.navigate(R.id.signInFragment)
        }

    }
}
