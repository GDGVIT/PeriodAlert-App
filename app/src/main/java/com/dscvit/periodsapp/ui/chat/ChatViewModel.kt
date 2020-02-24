package com.dscvit.periodsapp.ui.chat

import androidx.lifecycle.ViewModel
import com.dscvit.periodsapp.repository.AppRepository

class ChatViewModel(private val repo: AppRepository): ViewModel() {

    fun viewChatRooms() = repo.viewChatRooms()

    fun getMessages(chatRoomId: Int) = repo.getMessages(chatRoomId)

}