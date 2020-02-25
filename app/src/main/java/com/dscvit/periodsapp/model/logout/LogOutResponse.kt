package com.dscvit.periodsapp.model.logout


import com.google.gson.annotations.SerializedName

data class LogOutResponse(
    @SerializedName("Details")
    val details: Details,
    @SerializedName("message")
    val message: String
)