package com.dscvit.periodsapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.dscvit.periodsapp.utils.shortToast

private const val PERMISSION_REQUEST = 10

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPrefs = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                getLocation(this)
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                    if (requestAgain) {
                        Toast.makeText(
                            this,
                            "Permission is required to use this app",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Go to settings and enable the permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    getLocation(this)
                }
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun getLocation(context: Context) {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, object :
            LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location != null) {
                    val lat = location.latitude.toFloat()
                    val lon = location.longitude.toFloat()

                    sharedPrefs[Constants.PREF_CURR_LAT] = lat
                    sharedPrefs[Constants.PREF_CURR_LON] = lon

                    val intent = Intent(context, PreAuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) { getLocation(context) }

            override fun onProviderDisabled(provider: String?) {
                shortToast("Turn on Location")
            }
        }, Looper.getMainLooper())
    }
}
