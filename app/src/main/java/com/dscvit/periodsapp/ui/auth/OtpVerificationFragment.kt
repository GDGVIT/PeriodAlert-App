package com.dscvit.periodsapp.ui.auth


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.firebase.AuthHelper
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.utils.disable
import com.dscvit.periodsapp.utils.shortToast
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

        val sharedPrefs = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        otpMessageTextView.text = "Please enter the OTP we just sent to, \n${sharedPrefs.getString(
            Constants.PREF_PHONE_NUMBER,
            ""
        )}"

        requireActivity().onBackPressedDispatcher.addCallback(this) {}

        verifyButton.setOnClickListener {
            if (otpEditText.text.length == 6) {
                verifyButton.disable()

                val authHelper = AuthHelper(requireContext(), view, requireActivity())
                authHelper.authenticate(otpEditText)
            } else {
                requireContext().shortToast("Enter a six digit OTP")
            }
        }
    }
}
