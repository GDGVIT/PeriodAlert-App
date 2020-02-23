package com.dscvit.periodsapp.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.websocket.ChatWsListener
import kotlinx.android.synthetic.main.activity_chat.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ChatActivity : AppCompatActivity() {

    private lateinit var baseUrl: String
    private lateinit var ws: WebSocket
    private lateinit var client: OkHttpClient
    private lateinit var wsListener: ChatWsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val sharedPref = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
        val extras = intent.extras
        val authKey = sharedPref.getString(Constants.PREF_AUTH_KEY, "")
        val receiverId = extras?.getInt(Constants.PREF_RECEIVER_ID)
        val senderId = sharedPref.getInt(Constants.PREF_USER_ID, 0)

        client = OkHttpClient()
        wsListener = ChatWsListener()

        baseUrl = "${Constants.WS_BASE_URL}$authKey/$receiverId/1/"

        val request = Request.Builder().url(baseUrl).build()
        ws = client.newWebSocket(request, wsListener)

        sendButton.setOnClickListener {
            val msg = msgEditText.text.toString()
            ws.send("{\"message\": \"$msg\", \"sender_id\": $senderId, \"receiver_id\": $receiverId}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ws.close(1000, "Close Normal")
    }
}
