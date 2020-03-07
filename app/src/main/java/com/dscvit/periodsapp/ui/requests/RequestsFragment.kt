package com.dscvit.periodsapp.ui.requests


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.RequestListAdapter
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.model.requests.Request
import com.dscvit.periodsapp.repository.AppRepository
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.websocket.ChatWsListener
import kotlinx.android.synthetic.main.fragment_requests.*
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.*


class RequestsFragment : Fragment() {

    private val repo by inject<AppRepository>()
    private val requestsViewModel by viewModel<RequestsViewModel>()
    private lateinit var requestsList: List<Request>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestsProgressBar.hide()

        val sharedPrefs = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)
        val authKey = sharedPrefs.getString(Constants.PREF_AUTH_KEY, "")
        val senderId = sharedPrefs.getInt(Constants.PREF_USER_ID, 0)

        requestsToolbar.title = "Requests"

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

        //getLocationAndUpdateDb()

        val lat = sharedPrefs.getFloat(Constants.PREF_CURR_LAT, 0f).toDouble()
        val lon = sharedPrefs.getFloat(Constants.PREF_CURR_LON, 0f).toDouble()

        Log.d("esh", lat.toString())
        Log.d("esh", lon.toString())

        makeNetworkCallUpdateDb(lat, lon)

        repo.getAllRequests().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                noRequestTextView.show()
            } else {
                noRequestTextView.hide()
            }
            requestsList = it
            requestListAdapter.updateRequests(it)
        })

        val client = OkHttpClient()
        val wsListener: ChatWsListener by inject()

        requestsRecyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val receiverId = requestsList[position].userId

                val selTime = requestsList[position].dateTimeString.substring(11, 13).toInt()
                val selDate = requestsList[position].dateTimeString.substring(8, 10).toInt()
                val timeFormat = Calendar.getInstance()
                timeFormat.timeZone = TimeZone.getTimeZone("UTC")
                val currTime = timeFormat.get(Calendar.HOUR_OF_DAY)
                val currDate = timeFormat.get(Calendar.DATE)
                val timeDiff = currTime - selTime

                var isSameDay = false

                if (currDate == selDate) {
                    isSameDay = true
                }

                if (timeDiff < 3 && isSameDay) {
                    val baseUrl = "${Constants.WS_BASE_URL}$authKey/$receiverId/1/"
                    val request = okhttp3.Request.Builder().url(baseUrl).build()
                    val ws = client.newWebSocket(request, wsListener)
                    val msg = "Hi I am willing to help"
                    ws.send("{\"message\": \"$msg\", \"sender_id\": $senderId, \"receiver_id\": $receiverId}")
                    ws.close(1000, "Close Normal")

                    requestsViewModel.requestIsDone(requestsList[position].id)

                    requestsProgressBar.show()
                    requestsRecyclerView.hide()

                    Handler().postDelayed({
                        requestsProgressBar.hide()
                        findNavController().navigate(R.id.chatsFragment)
                    }, 1500)

                } else {
                    requireContext().shortToast("Request has expired")
                }
            }
        })
    }

    /*
    @SuppressWarnings("MissingPermission")
    private fun getLocationAndUpdateDb() {
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
                requireContext().shortToast("Turn on Location")
            }
        }, Looper.getMainLooper())
    }
     */

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
                                    alert.dateTimeCreation,
                                    0
                                )
                                requestsViewModel.upsertRequest(request)
                            }
                        }
                    }
                }
                Result.Status.ERROR -> {
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
