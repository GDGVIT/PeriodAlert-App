package com.dscvit.periodsapp.model.registernotification


import com.google.gson.annotations.SerializedName

data class RegisterNotificationRequest(
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("registration_id")
    val registrationId: String
)