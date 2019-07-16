package com.example.lesson9kislyakov.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import com.example.lesson9kislyakov.utils.Decompress
import com.example.lesson9kislyakov.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

const val CHANNEL_ID = "temperatureServiceChannel"

const val ACTION_INTENT = "com.example.lesson9kislyakov"
const val ACTION_DOWNLOAD = "test"
const val ACTION_UNZIP = "unzip"
const val ACTION_FINISH = "finish"

class DownloadService : Service() {
    var disposable: Disposable? = null

    companion object {
        fun createStartIntent(context: Context): Intent{
            return Intent(context, DownloadService::class.java)
        }
        const val DEFAULT_NOTIFICATION_ID = 101
        //just an empty intent
        val notificationIntent = Intent()
    }

    @SuppressLint("PrivateResource")
    override fun onCreate() {
        super.onCreate()
        Log.d("myTag", "Download onCreate")

        createNotificationChannel()

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this,
            CHANNEL_ID
        )
            .setContentTitle(getString(R.string.download_service_title))
            .setContentText(getString(R.string.download_service_message))
            .setSmallIcon(R.drawable.notification_icon_background)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(DEFAULT_NOTIFICATION_ID, notification)

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.foreground_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // We want this service to continue running until it is explicitly
        // stopped, so return
        Log.d("myTag", "Download onStart")
        downloadFile()

        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {

        Log.d("myTag", "onTaskRemoved: download service")
        super.onTaskRemoved(rootIntent)
        //stop service
        stopSelf()
    }


    override fun onDestroy() {
        Log.d("myTag", "onDestroy: download service")
        if (disposable is Disposable && disposable != null) {
            if(disposable?.isDisposed!!){
                disposable?.dispose()
            }
        }

        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @WorkerThread
    private fun downloadFile() {
        disposable = Flowable.fromCallable {
            val fileName = "IMAGE"
            val fileFolder: String
            val bufferIncomeSize = 8192
            val bufferOutSize = 8192
            var bufferCount: Int
            val url =
                URL("https://getfile.dokpub.com/yandex/get/https://yadi.sk/d/29-tRzXkN0U0ZQ")

            try {

                val connection = url.openConnection()
                connection.connect()
                // getting file length
                val lengthOfFile = connection.contentLength

                // input stream to read file - with 8k buffer
                val input = BufferedInputStream(url.openStream(), bufferIncomeSize)

                //External directory path to save file
                fileFolder =
                    Environment.getExternalStorageDirectory().toString() + File.separator

                val directory = File(fileFolder)
                val zipFile = File(fileFolder + fileName)
                Log.d("myTag", directory.absolutePath)

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                if (!zipFile.exists()) {
                    zipFile.createNewFile()
                }

                // Output stream to write file
                val output = FileOutputStream(fileFolder + fileName)
                val data = ByteArray(bufferOutSize)
                var total: Long = 0

                bufferCount = input.read(data)
                while (bufferCount != -1) {
                    total += bufferCount.toLong()
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    val intent = Intent(ACTION_INTENT)
                    intent.putExtra(ACTION_DOWNLOAD, (total * 100 / lengthOfFile).toInt())
                    sendBroadcast(intent)
                    // writing data to file
                    output.write(data, 0, bufferCount)
                    bufferCount = input.read(data)
                }

                // flushing output
                output.flush()

                // closing streams
                output.close()
                input.close()

                val zippedFileWay = fileFolder + fileName
                val unzipLocation = "$fileFolder/unzipped/"

                var finishMessage = Intent(ACTION_INTENT)
                finishMessage.putExtra(
                    ACTION_UNZIP, getString(
                        R.string.unzipping_button
                    ))
                sendBroadcast(finishMessage)

                val decompressInstance =
                    Decompress(zippedFileWay, unzipLocation)
                val resultFile = decompressInstance.unzip()

                finishMessage = Intent(ACTION_INTENT)
                finishMessage.putExtra(ACTION_FINISH, resultFile)
                sendBroadcast(finishMessage)

                Log.d("myTag", "Download finished")

                stopSelf()
                stopForeground(true)

            } catch (e: Exception) {
                Log.e("Error: ", e.message)
            }

            stopForeground(true)
            stopSelf()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

}