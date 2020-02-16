package com.dscvit.periodsapp.model.login


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone_no")
    val phoneNo: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("username")
    val username: String
)