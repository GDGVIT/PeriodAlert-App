package com.dscvit.periodsapp.model.requests


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class Request(
    val userId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}