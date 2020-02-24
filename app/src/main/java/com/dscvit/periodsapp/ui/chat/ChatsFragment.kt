package com.dscvit.periodsapp.ui.chat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.ChatListAdapter
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.utils.*
import kotlinx.android.synthetic.main.fragment_chats.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ChatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatViewModel by sharedViewModel<ChatViewModel>()
        val chatListAdapter = ChatListAdapter()
        chatRoomRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatListAdapter
        }

        chatProgressBar.show()
        chatRoomRecyclerView.hide()
        chatMsgTextView.hide()

        chatViewModel.viewChatRooms().observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Result.Status.LOADING -> { }
                Result.Status.SUCCESS -> {
                    if (it.data?.message == "Chat rooms found") {
                        val chatRooms = it.data.chatRooms
                        chatListAdapter.updateChats(chatRooms)
                        chatProgressBar.hide()
                        chatRoomRecyclerView.show()
                        chatMsgTextView.show()

                        chatRoomRecyclerView.addOnItemClickListener(object: OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                val intent = Intent(requireContext(), ChatActivity::class.java)
                                intent.putExtra(Constants.PREF_CHAT_ROOM_ID, chatRooms[position].id)
                                startActivity(intent)
                            }
                        })
                    }
                }
                Result.Status.ERROR -> {
                    if (it.message == "Network called failed with message HTTP 204 had non-zero Content-Length: 37") {
                        requireContext().shortToast("No Chats Available")
                    } else {
                        requireContext().shortToast("Cant get chats (Check internet)")
                    }
                    chatProgressBar.hide()
                    chatRoomRecyclerView.show()
                    chatMsgTextView.show()
                }
            }
        })
    }
}
