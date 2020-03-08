package com.dscvit.periodsapp.model.chat


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,
    @SerializedName("body")
    val body: String,
    @SerializedName("chat_room_id")
    val chatRoomId: Int,
    @SerializedName("date_time_creation")
    val dateTimeCreation: String,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("sender_id")
    val senderId: Int
)