package com.dscvit.periodsapp.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.model.requests.Request
import kotlinx.android.synthetic.main.requests_recycler_view_item.view.*
import java.util.*


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

            val selTime = request.dateTimeString.substring(11, 13).toInt()
            val selDate = request.dateTimeString.substring(8, 10).toInt()
            val timeFormat = Calendar.getInstance()
            timeFormat.timeZone = TimeZone.getTimeZone("UTC")
            val currTime = timeFormat.get(Calendar.HOUR_OF_DAY)
            val currDate = timeFormat.get(Calendar.DATE)
            val timeDiff = currTime - selTime

            var isSameDay = false

            if (currDate == selDate) {
                isSameDay = true
            }
            if (timeDiff < 3 && isSameDay) {
                bodyTextView.setTextColor(Color.parseColor("#228b22"))
                bodyTextView.text = "The request is active"
            } else {
                bodyTextView.setTextColor(Color.parseColor("#FF0000"))
                bodyTextView.text = "The request has expired"
            }
        }
    }
}