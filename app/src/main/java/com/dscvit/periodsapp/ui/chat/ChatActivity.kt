package com.dscvit.periodsapp.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.MessageListAdapter
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.websocket.ChatWsListener
import kotlinx.android.synthetic.main.activity_chat.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.koin.android.viewmodel.ext.android.viewModel

class ChatActivity : AppCompatActivity() {

//    private lateinit var baseUrl: String
//    private lateinit var ws: WebSocket
//    private lateinit var client: OkHttpClient
//    private lateinit var wsListener: ChatWsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val extras = intent.extras
        val chatRoomId = extras?.getInt(Constants.PREF_CHAT_ROOM_ID)

        val chatViewModel by viewModel<ChatViewModel>()
        val messageListAdapter = MessageListAdapter()
        messages_recycler_view.apply {
            adapter = messageListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        messagesProgressBar.show()
        messages_recycler_view.hide()
        sendMessageLayout.hide()

        chatViewModel.getMessages(chatRoomId?: 0).observe(this, Observer {
            when(it.status) {
                Result.Status.LOADING -> { }
                Result.Status.SUCCESS -> {
                    val messagesList = it.data?.messages
                    messageListAdapter.updateMessages(messagesList!!)
                    messagesProgressBar.hide()
                    messages_recycler_view.show()
                    sendMessageLayout.show()
                }
                Result.Status.ERROR -> {
                    shortToast("Error in getting messages")
                    messagesProgressBar.hide()
                    messages_recycler_view.show()
                    sendMessageLayout.show()
                }
            }
        })

        /*
        val sharedPref = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
        val extras = intent.extras
        val authKey = sharedPref.getString(Constants.PREF_AUTH_KEY, "")
        val receiverId = extras?.getInt(Constants.PREF_RECEIVER_ID)
        val senderId = sharedPref.getInt(Constants.PREF_USER_ID, 0)

        client = OkHttpClient()
        wsListener = ChatWsListener(msgsTextView)

        baseUrl = "${Constants.WS_BASE_URL}$authKey/$receiverId/1/"

        val request = Request.Builder().url(baseUrl).build()
        ws = client.newWebSocket(request, wsListener)

        sendButton.setOnClickListener {
            val msg = msgEditText.text.toString()
            ws.send("{\"message\": \"$msg\", \"sender_id\": $senderId, \"receiver_id\": $receiverId}")
        }
         */
    }

    /*
    override fun onDestroy() {
        super.onDestroy()
        ws.close(1000, "Close Normal")
    }
     */
}
