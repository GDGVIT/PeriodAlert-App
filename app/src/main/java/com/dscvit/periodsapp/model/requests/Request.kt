package com.dscvit.periodsapp.model.requests


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class Request(
    @PrimaryKey val id: Int,
    val userId: Int,
    val userName: String,
    val dateTimeString: String,
    val isDone: Int
)