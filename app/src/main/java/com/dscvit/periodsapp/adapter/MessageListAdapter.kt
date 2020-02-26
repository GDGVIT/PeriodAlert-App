package com.dscvit.periodsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.chat.Message
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import kotlinx.android.synthetic.main.my_message_recycler_view_item.view.*
import kotlinx.android.synthetic.main.other_message_recycler_view_item.view.*

private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2

class MessageListAdapter(context: Context) : RecyclerView.Adapter<MessageViewHolder>() {

    private var messagesList: List<Message> = mutableListOf()

    private val sharedPrefs = PreferenceHelper.customPrefs(context, Constants.PREF_NAME)
    private val userId = sharedPrefs.getInt(Constants.PREF_USER_ID, 0)

    fun updateMessages(newMessages: List<Message>) {
        messagesList = newMessages
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = messagesList[position]
        return if (userId == message.senderId) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.my_message_recycler_view_item,
                    parent,
                    false
                )
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.other_message_recycler_view_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messagesList[position])
    }

    override fun getItemCount(): Int = messagesList.size

    inner class MyMessageViewHolder(view: View) : MessageViewHolder(view) {
        private val msgBodyTextView = view.myMsgTextView

        override fun bind(msg: Message) {
            msgBodyTextView.text = msg.body
        }
    }

    inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private val msgBodyTextView = view.otherMsgTextView

        override fun bind(msg: Message) {
            msgBodyTextView.text = msg.body
        }
    }
}

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Message) {}
}