package com.dscvit.periodsapp.model.signup


import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_no")
    val phoneNo: String,
    @SerializedName("username")
    val username: String
)