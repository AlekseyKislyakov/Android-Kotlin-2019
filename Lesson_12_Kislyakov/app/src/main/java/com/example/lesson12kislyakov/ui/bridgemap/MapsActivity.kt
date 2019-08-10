package com.example.lesson12kislyakov.ui.bridgemap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.lesson12kislyakov.R
import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.ui.base.BaseActivity
import com.example.lesson12kislyakov.ui.bridgedetail.BridgeDetailActivity
import com.example.lesson12kislyakov.ui.bridgelist.BridgeListActivity
import com.example.lesson12kislyakov.utils.DivorceUtil
import com.example.lesson12kislyakov.utils.MapUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapsActivity : BaseActivity(), MapsMvpView, OnMapReadyCallback {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }
    }

    val REQUEST_CODE_PERMISSION = 101

    private val STATE_LOADING = 0
    private val STATE_DATA = 1
    private val STATE_ERROR = 2

    private var bridgeRowShown = false

    private val BRIDGE_ANCHOR_X = 0.5f
    private val BRIDGE_ANCHOR_Y = 0.5f

    private val ZOOM_NORMAL = 7.0f
    private val ZOOM_BIG = 9.0f
    private val ZOOM_INSANE = 11.0f

    private var lastClickedMarker: Marker? = null
    private var lastId = -1

    var bridgeList: List<Bridge> = listOf()

    @Inject
    lateinit var mMainPresenter: MapsPresenter

    private lateinit var mMap: GoogleMap

    private val mapsUtil = MapUtils()
    private val divorceUtil = DivorceUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        activityComponent()?.inject(this)

        mMainPresenter.attachView(this)
        mMainPresenter.loadBridges()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentGoogleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        buttonRetry.setOnClickListener { mMainPresenter.loadBridges() }

        toolbarMain.inflateMenu(R.menu.map_menu)
        toolbarMain.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.itemList ->
                    startActivity(BridgeListActivity.newIntent(this))
            }
            true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setPadding(0, 0, 0, 0)
        requestPermissionWithRationale()

        setMarkerListeners()

    }

    private fun updateCamera() {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                mapsUtil.getAverageLatLng(bridgeList),
                ZOOM_INSANE
            )
        )
    }

    override fun onBackPressed() {
        if (bridgeRowShown) {
            constraintLayoutBridgeRow.visibility = View.GONE
            mMap.setPadding(0, 0, 0, 0)
            bridgeRowShown = false
        } else {
            super.onBackPressed()
        }

    }

    private fun setMarkerListeners() {
        constraintLayoutBridgeRow.setOnClickListener {
            startActivity(
                BridgeDetailActivity.newIntent(
                    this,
                    lastId
                )
            )


        }
        mMap.setOnMarkerClickListener { marker ->
            val bridgeId = mapsUtil.markerToIntConverter(marker.id)
            lastClickedMarker?.setIcon(
                mapsUtil.getBitmapDescriptor(
                    applicationContext,
                    divorceUtil.setStatus(bridgeList[bridgeId].divorces)
                )
            )
            marker.setIcon(mapsUtil.getBitmapDescriptor(this, R.drawable.ic_open_with_black_24dp))
            lastClickedMarker = marker
            lastId = bridgeList[bridgeId].id

            constraintLayoutBridgeRow.visibility = View.VISIBLE
            mMap.setPadding(
                0,
                0,
                0,
                resources.getDimension(R.dimen.dimen_map_padding_bottom).toInt()
            )
            // get marker id that is one that applies bridge element
            // set up fields
            imageViewBridgeRow.setImageResource(divorceUtil.setStatus(bridgeList[bridgeId].divorces))

            textViewHeaderRow.text = bridgeList[bridgeId].name
            textViewDivorceRow.text = divorceUtil.divorceListToString(bridgeList[bridgeId].divorces)

            // set row viewed flag
            bridgeRowShown = true
            true

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

    private fun addMarkers() {
        for (i in 0 until bridgeList.size) {

            val marker = MarkerOptions()
                .position(LatLng(bridgeList[i].lat, bridgeList[i].lng))
                .title(bridgeList[i].name)
                .anchor(BRIDGE_ANCHOR_X, BRIDGE_ANCHOR_Y)

            marker.icon(
                mapsUtil.getBitmapDescriptor(
                    applicationContext,
                    divorceUtil.setStatus(bridgeList[i].divorces)
                )
            )

            mMap.addMarker(marker)
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

    override fun showLoadingError() {
        viewFlipper.displayedChild = STATE_ERROR
    }

    override fun showBridges(bridges: List<Bridge>) {
        bridgeList = bridges
        addMarkers()
        updateCamera()
        viewFlipper.displayedChild = STATE_DATA
    }

    override fun showSingleBridge(bridge: Bridge) {

    }

    override fun showProgressView() {
        viewFlipper.displayedChild = STATE_LOADING
    }
}
