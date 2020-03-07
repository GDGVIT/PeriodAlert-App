package com.dscvit.periodsapp.websocket

import android.content.Context
import android.util.Log
import com.dscvit.periodsapp.model.chat.Message
import com.dscvit.periodsapp.repository.AppRepository
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class ChatWsListener(context: Context, private val repo: AppRepository): WebSocketListener() {

    private val sharedPrefs = PreferenceHelper.customPrefs(context, Constants.PREF_NAME)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("esh", "Web Socket is open")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("esh", "Message Received: $text")

        val chatRoomId = sharedPrefs.getInt(Constants.PREF_CURR_CHAT_ROOM, 0)

        val messageObject: JsonObject = Gson().fromJson(text, JsonObject::class.java)
        val id = messageObject.get("id").asInt
        val body = messageObject.get("message").asString
        val receiverId = messageObject.get("receiver_id").asInt
        val senderId = messageObject.get("sender_id").asInt

        val message = Message(id, body, chatRoomId, "", receiverId, senderId)

        runBlocking {
            withContext(Dispatchers.IO) {
                repo.insertMessage(message)
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d("esh", "Error: ${t.message}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d("esh", "Closing: $code / $reason")
    }
}