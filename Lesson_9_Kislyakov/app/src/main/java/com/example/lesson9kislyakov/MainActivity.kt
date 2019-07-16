package com.example.lesson9kislyakov

import android.Manifest
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lesson9kislyakov.services.*
import com.example.lesson9kislyakov.utils.ServiceCallbacks
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


const val REQUEST_CODE_PERMISSION = 101

class MainActivity : AppCompatActivity(), ServiceCallbacks {
    override fun startWeatherUpdate(temperature: String) {
        textViewShowTemperature.text = temperature
    }

    lateinit var serviceConnection: ServiceConnection
    var broadcastReceiver: BroadcastReceiver? = null
    var bound = false
    var temperatureService: TemperatureService? = null
    var downloadText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) run {
            createRequestPermissionButton()

        } else {
            createDownloadButton()
        }

        setBroadcastReciever()
        startReciever()

        setServiceConnection()
    }

    private fun createRequestPermissionButton() {
        buttonMainDownload.text = getString(R.string.request_permission_string)
        buttonMainDownload.setOnClickListener { l ->
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun createDownloadButton() {
        buttonMainDownload.text = getString(R.string.download_button)
        buttonMainDownload.setOnClickListener {
            if (isServiceRunning(DownloadService::class.java)) run {
                Toast.makeText(
                    this, getString(R.string.download_service_is_running), Toast.LENGTH_SHORT
                ).show()
            } else {
                val downloadIntent = DownloadService.createStartIntent(this)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(downloadIntent)
                } else {
                    startService(downloadIntent)
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createDownloadButton()
                } else {
                    Snackbar
                        .make(
                            constraintLayoutMain,
                            getString(R.string.request_permission_in_setings),
                            Snackbar.LENGTH_LONG
                        )
                        .setAction(getString(R.string.yes_string)) {
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

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, TemperatureService::class.java)
        Log.d("myTag", "MainActivity bindService")
        applicationContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (bound) {
            if (broadcastReceiver != null) {
                Log.d("myTag", "MainActivity unregisterReceiver")
                unregisterReceiver(broadcastReceiver)
            }
            temperatureService?.setCallbacks(null) // unregister
            applicationContext.unbindService(serviceConnection)
            bound = false
        }
        super.onStop()
    }

    private fun setServiceConnection() {
        serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
                // cast the IBinder and get TemperatureService instance
                Log.d("myTag", "MainActivity onServiceConnected")
                val binder = iBinder as TemperatureService.LocalBinder
                val temperatureService = binder.service
                bound = true// register
                temperatureService.setCallbacks(this@MainActivity)
            }


            override fun onServiceDisconnected(arg0: ComponentName) {
                bound = false
            }
        }
    }

    private fun startReciever() {
        val intFilter = IntentFilter(ACTION_INTENT)
        registerReceiver(broadcastReceiver, intFilter)
    }

    private fun setBroadcastReciever() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.hasExtra(ACTION_DOWNLOAD)) {
                    downloadText =
                        getString(R.string.downloading_progress) + intent.getIntExtra(
                            ACTION_DOWNLOAD,
                            0
                        ) + "%"
                    buttonMainDownload.text = downloadText
                }
                if (intent.hasExtra(ACTION_UNZIP)) {
                    downloadText = getString(R.string.unzipping_progress)
                    buttonMainDownload.text = downloadText
                }
                if (intent.hasExtra(ACTION_FINISH)) {
                    buttonMainDownload.text = getString(R.string.download_button)

                    Log.d("myTag", "Finish + ${intent.getStringExtra(ACTION_FINISH)}")

                    val bmp = BitmapFactory.decodeFile(intent.getStringExtra(ACTION_FINISH))
                    imageViewZipPicture.setImageBitmap(bmp)
                }
            }
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
