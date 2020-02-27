package com.dscvit.periodsapp.ui.requests


import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.RequestListAdapter
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.model.requests.Request
import com.dscvit.periodsapp.repository.AppRepository
import com.dscvit.periodsapp.utils.hide
import com.dscvit.periodsapp.utils.shortToast
import com.dscvit.periodsapp.utils.show
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import kotlin.math.*

class RequestsFragment : Fragment() {

    private val repo by inject<AppRepository>()
    private val requestsViewModel by sharedViewModel<RequestsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestListAdapter = RequestListAdapter()
        requestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestListAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        getLocationAndUpdateDb()

        repo.getAllRequests().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                noRequestTextView.show()
            } else {
                noRequestTextView.hide()
            }
            requestListAdapter.updateRequests(it)
        })
    }

    @SuppressWarnings("MissingPermission")
    private fun getLocationAndUpdateDb() {
        requestsProgressBar.show()

        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, object :
            LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    makeNetworkCallUpdateDb(lat, lon)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) {}

            override fun onProviderDisabled(provider: String?) {
                requestsProgressBar.hide()
                requireContext().shortToast("Turn on Location")
            }
        }, Looper.getMainLooper())
    }

    private fun makeNetworkCallUpdateDb(lat: Double, lon: Double) {
        requestsViewModel.getAlerts().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                }
                Result.Status.SUCCESS -> {
                    if (it.data?.message == "Received Alert") {
                        for (alert in it.data.alert) {
                            if (calculateDistance(lat, lon, alert.latitude, alert.longitude) <= 5) {
                                val request = Request(
                                    alert.id,
                                    alert.userId,
                                    alert.userUsername,
                                    alert.dateTimeCreation
                                )
                                requestsViewModel.upsertRequest(request)
                            }
                        }
                        requestsProgressBar.hide()
                    }
                }
                Result.Status.ERROR -> {
                    requestsProgressBar.hide()
                    requireContext().shortToast("Error in getting alerts")
                    Log.d("esh", it.message)
                }
            }
        })
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371
        val dLat = degToRad(lat1) - degToRad(lat2)
        val dLon = degToRad(lon1) - degToRad(lon2)
        val temp =
            sin(dLat / 2).pow(2) + cos(degToRad(lat1)) * cos(degToRad(lat2)) * (sin(dLon / 2).pow(2))

        return r * (2 * asin(sqrt(temp)))
    }

    private fun degToRad(deg: Double): Double {
        return (deg * PI / 180)
    }
}
