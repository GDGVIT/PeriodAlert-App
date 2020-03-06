package com.dscvit.periodsapp.ui.chat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.ChatListAdapter
import com.dscvit.periodsapp.model.Result
import com.dscvit.periodsapp.model.chat.ChatRoom
import com.dscvit.periodsapp.utils.*
import kotlinx.android.synthetic.main.fragment_chats.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ChatsFragment : Fragment() {

    private lateinit var chatRooms: List<ChatRoom>
    private val chatViewModel by sharedViewModel<ChatViewModel>()
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatsToolbar.title = "Chats"

        val sharedPrefs = PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)
        val userId = sharedPrefs.getInt(Constants.PREF_USER_ID, 0)

        chatListAdapter = ChatListAdapter(requireContext())
        chatRoomRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatListAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        chatProgressBar.show()
        chatRoomRecyclerView.hide()

        chatViewModel.viewChatRooms().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                }
                Result.Status.SUCCESS -> {
                    chatRooms = it.data!!

                    chatListAdapter.updateChats(chatRooms)
                    chatProgressBar.hide()
                    chatRoomRecyclerView.show()
                }
                Result.Status.ERROR -> {
                    Log.d("esh", it.message)
                    if (it.message == "Network called failed with message HTTP 204 had non-zero Content-Length: 37") {
                        requireContext().shortToast("No Chats Available")
                    } else {
                        requireContext().shortToast("Cant get chats (Check internet)")
                    }
                    chatProgressBar.hide()
                    chatRoomRecyclerView.show()
                }
            }
        })

        chatRoomRecyclerView.addOnItemClickListener(object : OnItemClickListener {
            var receiverId = 0
            var receiverName = ""
            override fun onItemClicked(position: Int, view: View) {
                if (chatRooms[position].participant1Id == userId) {
                    receiverId = chatRooms[position].participant2Id
                    receiverName = chatRooms[position].participant2Username
                } else {
                    receiverId = chatRooms[position].participant1Id
                    receiverName = chatRooms[position].participant1Username
                }

                val intent = Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra(Constants.EXTRA_CHAT_ROOM_ID, chatRooms[position].id)
                intent.putExtra(Constants.EXTRA_RECEIVER_ID, receiverId)
                intent.putExtra(Constants.EXTRA_RECEIVER_NAME, receiverName)
                startActivity(intent)
            }
        })
    }
}
