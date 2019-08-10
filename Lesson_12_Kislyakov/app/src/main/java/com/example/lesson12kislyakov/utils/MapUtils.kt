package com.example.lesson12kislyakov.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.lesson12kislyakov.data.models.Bridge
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

class MapUtils {

    private val MARKER_FIRST_DIGIT = 1

    fun getBitmapDescriptor(context: Context, id: Int): BitmapDescriptor {
        val vectorDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getDrawable(context, id) as VectorDrawable

        } else {
            VectorDrawableCompat.create(context.resources, id, null)
        }
        val h = vectorDrawable?.intrinsicHeight
        val w = vectorDrawable?.intrinsicWidth

        w?.let { h?.let { it1 -> vectorDrawable.setBounds(0, 0, it, it1) } }

        val bm = w.let { h.let { it1 -> Bitmap.createBitmap(it!!, it1!!, Bitmap.Config.ARGB_8888) } }
        val canvas = Canvas(bm)
        vectorDrawable?.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bm)

    }

    fun getAverageLatLng(bridgeList: List<Bridge>): LatLng {
        var lat = 0.0
        var lng = 0.0

        for (i in 0 until bridgeList.size) {
            lat += bridgeList[i].lat
            lng += bridgeList[i].lng
        }

        lat /= bridgeList.size
        lng /= bridgeList.size

        return LatLng(lat, lng)
    }


    fun markerToIntConverter(markerId: String): Int {
        // marker index looks as m** where ** are digits
        // so we get substring from position 1
        return Integer.parseInt(markerId.substring(MARKER_FIRST_DIGIT))
    }
}