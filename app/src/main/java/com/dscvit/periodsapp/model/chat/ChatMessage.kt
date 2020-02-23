package com.dscvit.periodsapp.model.chat


import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("message")
    val message: String,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("sender_id")
    val senderId: Int
)