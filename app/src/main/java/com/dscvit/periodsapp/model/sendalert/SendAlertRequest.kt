package com.dscvit.periodsapp.model.sendalert


import com.google.gson.annotations.SerializedName

data class SendAlertRequest(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)