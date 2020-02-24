package com.dscvit.periodsapp.model.chat


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chatrooms")
data class ChatRoom(
    @SerializedName("date_time_creation")
    val dateTimeCreation: String,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_message_time")
    val lastMessageTime: String,
    @SerializedName("participant1_id")
    val participant1Id: Int,
    @SerializedName("participant1_username")
    val participant1Username: String,
    @SerializedName("participant2_id")
    val participant2Id: Int,
    @SerializedName("participant2_username")
    val participant2Username: String
)