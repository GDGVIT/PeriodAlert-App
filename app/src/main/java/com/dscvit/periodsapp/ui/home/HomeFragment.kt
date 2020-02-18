package com.dscvit.periodsapp.ui.home


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
import com.dscvit.periodsapp.ui.PreAuthActivity
import com.dscvit.periodsapp.ui.auth.AuthViewModel
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authViewModel by sharedViewModel<AuthViewModel>()

        val sharedPreferences = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        signOutButton.setOnClickListener {
            signOutButton.disable()
            authViewModel.logOut().observe(viewLifecycleOwner, Observer {
                when(it.status) {
                    Result.Status.LOADING -> { }
                    Result.Status.SUCCESS -> {
                        if(it.data?.message == "User logged out") {
                            FirebaseAuth.getInstance().signOut()

                            sharedPreferences[Constants.PREF_IS_LOGGED_IN] = false
                            sharedPreferences[Constants.PREF_TOKEN_IS_UPDATED] = false

                            val intent = Intent(requireContext(), PreAuthActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Result.Status.ERROR -> {
                        requireContext().shortToast("Error in logging out")
                        Log.d("esh", it.message)

                        signOutButton.enable()
                    }
                }
            })
        }
    }
}
