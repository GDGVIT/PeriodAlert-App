package com.dscvit.periodsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.model.chat.Message
import com.dscvit.periodsapp.model.requests.Request

@Database(
    entities = [Message::class, ChatRoom::class, Request::class],
    version = 5,
    exportSchema = false
)
abstract class ChatsDatabase : RoomDatabase() {
    abstract fun chatsDao(): ChatsDao
}