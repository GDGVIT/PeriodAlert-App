package com.dscvit.periodsapp.ui.auth


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.ui.PostAuthActivity
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import kotlinx.android.synthetic.main.fragment_details.*

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

        sharedPreferences = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        requireActivity().onBackPressedDispatcher.addCallback(this) {}

        finishButton.setOnClickListener {
            sharedPreferences[Constants.PREF_IS_LOGGED_IN] = true

            val intent = Intent(requireContext(), PostAuthActivity::class.java)
            startActivity(intent)
        }
    }
}
