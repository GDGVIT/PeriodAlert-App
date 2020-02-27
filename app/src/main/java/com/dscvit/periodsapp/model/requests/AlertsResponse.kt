package com.dscvit.periodsapp.model.requests


import com.google.gson.annotations.SerializedName

data class AlertsResponse(
    @SerializedName("Alert")
    val alert: List<Alert>,
    @SerializedName("message")
    val message: String
)