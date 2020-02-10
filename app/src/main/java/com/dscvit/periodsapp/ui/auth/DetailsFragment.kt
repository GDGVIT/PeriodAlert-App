package com.dscvit.periodsapp.ui.auth


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.ui.PostAuthActivity
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {}

        finishButton.setOnClickListener {
            val intent = Intent(requireContext(), PostAuthActivity::class.java)
            startActivity(intent)
        }
    }
}
