package com.dscvit.periodsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.chat.Message
import kotlinx.android.synthetic.main.message_recycler_view_item.view.*

class MessageListAdapter: RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {

    private var messagesList: List<Message> = mutableListOf()

    fun updateMessages(newMessages: List<Message>) {
        messagesList = newMessages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.message_recycler_view_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messagesList[position])
    }

    override fun getItemCount(): Int = messagesList.size

    class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val msgBodyTextView = view.msgBodyTextView

        fun bind(msg: Message) {
            msgBodyTextView.text = msg.body
        }
    }
}