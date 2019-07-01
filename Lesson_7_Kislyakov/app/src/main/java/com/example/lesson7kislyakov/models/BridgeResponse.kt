package com.example.lesson7kislyakov.models

import java.util.*

data class BridgeResponse(
    val objects: List<Bridge>
)

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
)

data class Divorce(
    val end: Date,
    val id: Int,
    val start: Date
)