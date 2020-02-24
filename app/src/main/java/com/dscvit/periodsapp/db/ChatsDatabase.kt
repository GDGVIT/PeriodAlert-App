package com.dscvit.periodsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.model.chat.Message

@Database(entities = [Message::class, ChatRoom::class], version = 1, exportSchema = false)
abstract class ChatsDatabase : RoomDatabase() {
    abstract fun chatsDao(): ChatsDao
}