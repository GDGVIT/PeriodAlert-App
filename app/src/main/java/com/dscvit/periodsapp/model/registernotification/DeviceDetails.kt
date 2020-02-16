package com.dscvit.periodsapp.model.registernotification


import com.google.gson.annotations.SerializedName

data class DeviceDetails(
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("registration_id")
    val registrationId: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("user")
    val user: Int
)