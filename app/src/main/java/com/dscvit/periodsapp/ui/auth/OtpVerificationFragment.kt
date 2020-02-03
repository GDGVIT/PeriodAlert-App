package com.dscvit.periodsapp.ui.auth


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.firebase.AuthHelper
import kotlinx.android.synthetic.main.fragment_otp_verification.*

class OtpVerificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyButton.setOnClickListener {
            val authHelper = AuthHelper(requireContext(), view, requireActivity())
            authHelper.authenticate(otpEditText)
        }
    }
}
