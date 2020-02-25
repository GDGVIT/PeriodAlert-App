package com.dscvit.periodsapp.model.chat


import com.google.gson.annotations.SerializedName

data class ChatRoomResponse(
    @SerializedName("ChatRooms")
    val chatRooms: List<ChatRoom>,
    @SerializedName("message")
    val message: String
)