package com.dscvit.periodsapp.model.sendalert


import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("date_time_creation")
    val dateTimeCreation: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("user_id")
    val userId: Int
)