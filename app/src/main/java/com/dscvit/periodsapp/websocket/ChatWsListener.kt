package com.dscvit.periodsapp.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.dscvit.periodsapp.utils.show
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class ChatWsListener(private val view: View): WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("esh", "Web Socket is open")

        Handler(Looper.getMainLooper()).post {
            view.show()
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("esh", "Message Received: $text")
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