package com.dscvit.periodsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.requests.Request
import kotlinx.android.synthetic.main.requests_recycler_view_item.view.*

class RequestListAdapter : RecyclerView.Adapter<RequestListAdapter.RequestViewHolder>() {

    private var requestList: List<Request> = mutableListOf()

    fun updateRequests(newRequests: List<Request>) {
        requestList = newRequests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RequestViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.requests_recycler_view_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int = requestList.size

    class RequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.titleTextView
        private val bodyTextView = view.bodyTextView

        fun bind(request: Request) {
            titleTextView.text = "${request.userName} has requested for help"
            bodyTextView.text = "Date & Time: ${request.dateTimeString}"
        }
    }
}