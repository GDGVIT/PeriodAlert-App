package com.dscvit.periodsapp.ui.home


import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.model.sendalert.SendAlertRequest
import com.dscvit.periodsapp.ui.PreAuthActivity
import com.dscvit.periodsapp.ui.chat.ChatViewModel
import com.dscvit.periodsapp.ui.requests.RequestsViewModel
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val homeViewModel by sharedViewModel<HomeViewModel>()
    private val chatViewModel by sharedViewModel<ChatViewModel>()
    private val requestsViewModel by sharedViewModel<RequestsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        homeProgressBar.hide()

        helpButton.setOnClickListener {
            helpButton.disable()
            homeProgressBar.show()
            helpButton.hide()
            signOutButton.hide()

            getLocationAndSendAlert()
        }

        signOutButton.setOnClickListener {
            signOutButton.disable()
            homeViewModel.logOut().observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.message == "User logged out") {
                            FirebaseAuth.getInstance().signOut()

                            sharedPreferences[Constants.PREF_IS_LOGGED_IN] = false
                            sharedPreferences[Constants.PREF_TOKEN_IS_UPDATED] = false

                            lifecycleScope.launch {
                                chatViewModel.deleteChatRooms()
                                chatViewModel.deleteMessages()
                                requestsViewModel.deleteRequests()
                            }

                            val intent = Intent(requireContext(), PreAuthActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
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

    @SuppressWarnings("MissingPermission")
    private fun getLocationAndSendAlert() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, object: LocationListener{
            override fun onLocationChanged(location: Location?) {
                if(location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    sendAlert(lat, lon)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) {}

            override fun onProviderDisabled(provider: String?) {
                helpButton.show()
                helpButton.enable()
                homeProgressBar.hide()
                signOutButton.show()
                requireContext().shortToast("Turn on Location")
            }
        }, Looper.getMainLooper())
    }

    private fun sendAlert(lat: Double, lon: Double) {
        val sendAlertRequest = SendAlertRequest(lat, lon)
        homeViewModel.sendAlert(sendAlertRequest).observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Result.Status.LOADING -> { }
                Result.Status.SUCCESS -> {
                    if(it.data?.message == "Sent notificaion") {
                        helpButton.show()
                        helpButton.enable()
                        homeProgressBar.hide()
                        signOutButton.show()
                        requireContext().shortToast("Alert Sent!")
                    }
                }
                Result.Status.ERROR -> {
                    requireContext().shortToast("Error in sending alert")
                    helpButton.show()
                    helpButton.enable()
                    homeProgressBar.hide()
                    signOutButton.show()
                    Log.d("esh", it.message)
                }
            }
        })
    }
}
