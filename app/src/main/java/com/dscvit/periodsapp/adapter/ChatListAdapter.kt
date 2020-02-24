package com.dscvit.periodsapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.ui.chat.ChatActivity
import kotlinx.android.synthetic.main.chats_recycler_view_item.view.*

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    private var chatsList: List<ChatRoom> = mutableListOf()

    fun updateChats(newChats: List<ChatRoom>) {
        chatsList = newChats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.chats_recycler_view_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatsList[position])

        holder.itemView.setOnClickListener {
            
        }
    }

    override fun getItemCount() = chatsList.size

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.titleTextView
        private val bodyTextView = view.bodyTextView

        fun bind(chat: ChatRoom) {
            titleTextView.text = chat.participant2Username
            bodyTextView.text = chat.participant2Id.toString()
        }
    }
}