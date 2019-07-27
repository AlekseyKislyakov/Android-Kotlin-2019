package com.example.lesson10kislyakov

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.lesson10kislyakov.models.Bridge
import com.example.lesson10kislyakov.network.BridgeApiService
import com.example.lesson10kislyakov.utils.DivorceUtil
import com.example.lesson10kislyakov.utils.MapUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_maps.*

const val REQUEST_CODE_PERMISSION = 101

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val STATE_LOADING = 0
    private val STATE_DATA = 1
    private val STATE_ERROR = 2

    private val BRIDGE_ANCHOR_X = 0.5f
    private val BRIDGE_ANCHOR_Y = 0.5f

    private val ZOOM_NORMAL = 7.0f
    private val ZOOM_BIG = 9.0f
    private val ZOOM_INSANE = 11.0f

    // Below are fused location variables
    private val REQUEST_CHECK_SETTINGS = 0x1

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

    private var bridgeRowShown = false

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mSettingsClient: SettingsClient

    private var mLocationRequest = LocationRequest()

    private lateinit var mLocationSettingsRequest: LocationSettingsRequest

    private lateinit var mLocationCallback: LocationCallback

    private lateinit var mCurrentLocation: Location

    private var locationMarker: Marker? = null
    private var lastClickedMarker: Marker? = null

    private var mRequestingLocationUpdates: Boolean = false

    var bridgeList: List<Bridge> = listOf()

    private val divorceUtil = DivorceUtil()
    private val mapsUtil = MapUtils()

    private var disposable: Disposable? = null

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        getBridges()
        buttonRetry.setOnClickListener { getBridges() }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentGoogleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewFlipper.displayedChild = STATE_DATA
        mMap = googleMap
        mMap.setPadding(0,0,0,resources.getDimension(R.dimen.dimen_map_padding_bottom).toInt())

        requestPermissionWithRationale()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()

        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true
            startLocationUpdates()
        }
        setMarkerListeners()
    }

    override fun onResume() {
        super.onResume()
        if (mRequestingLocationUpdates && checkLocationPermission()) {
            startLocationUpdates()
        } else if (!checkLocationPermission()) {
            Toast.makeText(this, "Location permission is not given", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onBackPressed() {
        if (bridgeRowShown) {
            constraintLayoutBridgeRow.visibility = View.GONE
            bridgeRowShown = false
        } else {
            super.onBackPressed()
        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener {
                if (checkLocationPermission()) {
                    mFusedLocationClient.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback, Looper.myLooper()
                    )
                }
                Log.d("myTag", "Location updates (fused) started")
            }
            .addOnFailureListener {
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {

                        Log.d(
                            "myTag", "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = it as ResolvableApiException
                            rae.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            Log.i("myTag", "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                        Log.e("myTag", errorMessage)
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        mRequestingLocationUpdates = false
                    }

                }
                Log.d("myTag", "START LOCATION fail" + it.localizedMessage)
                mRequestingLocationUpdates = false
            }
    }

    private fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d("myTag", "stopLocationUpdates: updates never requested, no-op.")
            return
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener {
                //mRequestingLocationUpdates = false
                Log.d("myTag", "Location updates stopped")
            }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                val mCurrentLocation = locationResult!!.lastLocation
                val mCurrentLatLng = LatLng(mCurrentLocation!!.latitude, mCurrentLocation.longitude)
                if (locationMarker == null)
                    locationMarker = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude))
                            .anchor(0.5.toFloat(), 0.5.toFloat())
                            .icon(mapsUtil.getBitmapDescriptor(applicationContext, R.drawable.ic_my_location_blue_24dp))
                    )
                else {
                    locationMarker!!.position =
                        LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                }
                Log.d(
                    "myTag",
                    "Current Location Latitude: " + mCurrentLatLng.latitude + " Longitude: " +
                            mCurrentLatLng.longitude
                )
            }
        }

    }

    private fun checkLocationPermission(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun createLocationRequestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), REQUEST_CODE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocationEnabledOrMakeRequest()
                } else {
                    Snackbar
                        .make(
                            constraintLayoutMain,
                            getString(R.string.request_permission_in_setings),
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setAction(getString(R.string.settings_string)) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }.show()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationEnabledOrMakeRequest() {
        if (!checkLocationPermission()
        ) {
            createLocationRequestPermission()
        } else {
            mMap.isMyLocationEnabled = true
        }
    }

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            val message = getString(R.string.location_show_rationale)
            Snackbar.make(constraintLayoutMain, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.string_ok)) {}
                .show()
            setLocationEnabledOrMakeRequest()
        } else {
            setLocationEnabledOrMakeRequest()
        }
    }

    private fun updateCamera() {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                mapsUtil.getAverageLatLng(bridgeList),
                ZOOM_INSANE
            )
        )
    }

    private fun addMarkers() {
        for (i in 0 until bridgeList.size) {

            val marker = MarkerOptions()
                .position(LatLng(bridgeList[i].lat, bridgeList[i].lng))
                .title(bridgeList[i].name)
                .anchor(BRIDGE_ANCHOR_X, BRIDGE_ANCHOR_Y)

            when (divorceUtil.divorceState(bridgeList[i].divorces)) {
                divorceUtil.STATE_OPEN -> marker.icon(
                    mapsUtil.getBitmapDescriptor(
                        applicationContext,
                        R.drawable.ic_brige_normal
                    )
                )
                divorceUtil.STATE_NEAR -> marker.icon(mapsUtil.getBitmapDescriptor(applicationContext, R.drawable.ic_brige_soon))
                divorceUtil.STATE_CLOSED -> marker.icon(
                    mapsUtil.getBitmapDescriptor(
                        applicationContext,
                        R.drawable.ic_brige_late
                    )
                )
            }

            mMap.addMarker(marker)
        }
    }

    private fun setMarkerListeners() {
        constraintLayoutBridgeRow.setOnClickListener {
            Toast.makeText(this, textViewHeaderRow.text, Toast.LENGTH_LONG).show()
        }
        mMap.setOnMarkerClickListener { marker ->
            if (marker.id == locationMarker?.id) {
                true
            } else {
                val bridgeId = mapsUtil.markerToIntConverter(marker.id)
                if (lastClickedMarker != null) {
                    when (divorceUtil.divorceState(bridgeList[bridgeId].divorces)) {
                        divorceUtil.STATE_OPEN -> lastClickedMarker!!.setIcon(
                            mapsUtil.getBitmapDescriptor(
                                applicationContext,
                                R.drawable.ic_brige_normal
                            )
                        )
                        divorceUtil.STATE_NEAR -> lastClickedMarker!!.setIcon(
                            mapsUtil.getBitmapDescriptor(
                                applicationContext,
                                R.drawable.ic_brige_soon
                            )
                        )
                        divorceUtil.STATE_CLOSED -> lastClickedMarker!!.setIcon(
                            mapsUtil.getBitmapDescriptor(
                                applicationContext,
                                R.drawable.ic_brige_late
                            )
                        )
                    }
                }

                marker.setIcon(mapsUtil.getBitmapDescriptor(this, R.drawable.ic_open_with_black_24dp));
                lastClickedMarker = marker

                constraintLayoutBridgeRow.visibility = View.VISIBLE

                // get marker id that is one that applies bridge element


                // set up fields
                when (divorceUtil.divorceState(bridgeList[bridgeId].divorces)) {
                    divorceUtil.STATE_OPEN -> imageViewBridgeRow.setImageResource(R.drawable.ic_brige_normal)
                    divorceUtil.STATE_NEAR -> imageViewBridgeRow.setImageResource(R.drawable.ic_brige_soon)
                    divorceUtil.STATE_CLOSED -> imageViewBridgeRow.setImageResource(R.drawable.ic_brige_late)
                }

                textViewHeaderRow.text = bridgeList[bridgeId].name
                textViewDivorceRow.text = divorceUtil.divorceListToString(bridgeList[bridgeId].divorces)

                // set row viewed flag
                bridgeRowShown = true
                true
            }
        }
    }

    private fun getBridges() {
        viewFlipper.displayedChild = STATE_LOADING
        disposable = BridgeApiService.getRetrofit()
            .getSingleBridgeResponse()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                bridgeList = result.objects

                addMarkers()
                updateCamera()

                viewFlipper.displayedChild = STATE_DATA
            }, { error ->
                viewFlipper.displayedChild = STATE_ERROR
                error.printStackTrace()
            })
    }
}
