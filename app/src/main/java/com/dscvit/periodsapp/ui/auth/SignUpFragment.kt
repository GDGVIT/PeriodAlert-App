package com.dscvit.periodsapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.firebase.AuthHelper
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

        // Gets the phone number and sends otp using firebase
        sendOtpButton.setOnClickListener {
            val authHelper = AuthHelper(requireContext(), view, requireActivity())
            authHelper.sendOtp(phoneNumberEditText)
        }

    }
}
