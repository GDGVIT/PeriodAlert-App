package com.dscvit.periodsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.model.chat.Message
import com.dscvit.periodsapp.model.requests.Request

@Dao
interface ChatsDao {
    @Query("SELECT * FROM chatrooms")
    fun getAllChatRooms(): LiveData<List<ChatRoom>>

    @Query("SELECT * FROM messages WHERE chatRoomId = :chatId")
    fun loadMessagesByChatRoomId(chatId: Int): LiveData<List<Message>>

    @Query("SELECT * FROM requests WHERE isDone=0 ORDER BY id DESC")
    fun getAllRequests(): LiveData<List<Request>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChatRooms(chatRooms: List<ChatRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertRequest(request: Request)

    @Query("UPDATE requests SET isDone=1 WHERE id= :id")
    suspend fun requestIsDone(id: Int)

    @Query("DELETE FROM chatrooms")
    suspend fun deleteChatRooms()

    @Query("DELETE FROM messages")
    suspend fun deleteMessages()

    @Query("DELETE FROM requests")
    suspend fun deleteRequests()
}