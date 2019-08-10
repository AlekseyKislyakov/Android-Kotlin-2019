package com.example.lesson12kislyakov.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class BridgeResponse(
    val objects: List<Bridge>
)

@Parcelize
data class Bridge(
    val description: String,
    val description_eng: String,
    val divorces: List<Divorce>,
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String,
    val name_eng: String,
    val photo_close: String,
    val photo_open: String,
    val resource_uri: String
) :Parcelable

@Parcelize
data class Divorce(
    val end: Date,
    val id: Int,
    val start: Date
) :Parcelable