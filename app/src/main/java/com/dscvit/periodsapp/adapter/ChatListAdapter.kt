package com.dscvit.periodsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import kotlinx.android.synthetic.main.chats_recycler_view_item.view.*

class ChatListAdapter(context: Context) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    private var chatsList: List<ChatRoom> = mutableListOf()
    private val sharedPrefs = PreferenceHelper.customPrefs(context, Constants.PREF_NAME)

    private val userId = sharedPrefs.getInt(Constants.PREF_USER_ID, 0)

    fun updateChats(newChats: List<ChatRoom>) {
        chatsList = newChats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.chats_recycler_view_item,
            parent,
            false
        ),
        userId
    )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatsList[position])
    }

    override fun getItemCount() = chatsList.size

    class ChatViewHolder(view: View, private val userId: Int) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.titleTextView

        fun bind(chat: ChatRoom) {
            val username: String

            if (userId == chat.participant1Id) {
                username = chat.participant2Username
            } else {
                username = chat.participant1Username
            }

            titleTextView.text = username
        }
    }
}