package com.dscvit.periodsapp.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.MessageListAdapter
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.dscvit.periodsapp.websocket.ChatWsListener
import kotlinx.android.synthetic.main.activity_chat.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var baseUrl: String
    private lateinit var ws: WebSocket
    private lateinit var client: OkHttpClient
    private val chatViewModel by viewModel<ChatViewModel>()
    private lateinit var messageListAdapter: MessageListAdapter
    private var chatRoomId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val sharedPref = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)

        val extras = intent.extras
        chatRoomId = extras?.getInt(Constants.EXTRA_CHAT_ROOM_ID)
        sharedPref[Constants.PREF_CURR_CHAT_ROOM] = chatRoomId
        val senderId = sharedPref.getInt(Constants.PREF_USER_ID, 0)
        val receiverId = extras?.getInt(Constants.EXTRA_RECEIVER_ID)
        val authKey = sharedPref.getString(Constants.PREF_AUTH_KEY, "")


        messageListAdapter = MessageListAdapter()
        messages_recycler_view.apply {
            adapter = messageListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        messagesProgressBar.show()
        messages_recycler_view.hide()
        sendMessageLayout.hide()

        chatViewModel.getMessages(chatRoomId ?: 0).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                }
                Result.Status.SUCCESS -> {
                    val messagesList = it.data

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

        client = OkHttpClient()
        val wsListener: ChatWsListener by inject()

        baseUrl = "${Constants.WS_BASE_URL}$authKey/$receiverId/1/"

        val request = Request.Builder().url(baseUrl).build()
        ws = client.newWebSocket(request, wsListener)

        sendMessageButton.setOnClickListener {
            val msg = messageEditText.text.toString()
            ws.send("{\"message\": \"$msg\", \"sender_id\": $senderId, \"receiver_id\": $receiverId}")
            messageEditText.setText("")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ws.close(1000, "Close Normal")
    }
}
