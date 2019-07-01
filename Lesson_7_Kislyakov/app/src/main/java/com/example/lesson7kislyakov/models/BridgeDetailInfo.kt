package com.example.lesson7kislyakov.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BridgeDetailInfo(val bridgeName: String,
                            val bridgeDivorces: String,
                            val bridgeDescription: String,
                            val bridgePhoto: String,
                            val bridgeState: Int) : Parcelable