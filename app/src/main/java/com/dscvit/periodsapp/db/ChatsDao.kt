package com.dscvit.periodsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.model.chat.Message

@Dao
interface ChatsDao {
    @Query("SELECT * FROM chatrooms")
    fun getAllChatRooms(): LiveData<List<ChatRoom>>

    @Query("SELECT * FROM messages WHERE chatRoomId = :chatId")
    fun loadMessagesByChatRoomId(chatId: Int): LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChatRooms(chatRooms: List<ChatRoom>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: Message)

    @Query("DELETE FROM chatrooms")
    suspend fun deleteChatRooms()

    @Query("DELETE FROM messages")
    suspend fun deleteMessages()
}