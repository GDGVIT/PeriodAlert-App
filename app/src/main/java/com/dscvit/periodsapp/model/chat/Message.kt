package com.dscvit.periodsapp.model.chat


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("body")
    val body: String,
    @SerializedName("chat_room_id")
    val chatRoomId: Int,
    @SerializedName("date_time_creation")
    val dateTimeCreation: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("sender_id")
    val senderId: Int
)