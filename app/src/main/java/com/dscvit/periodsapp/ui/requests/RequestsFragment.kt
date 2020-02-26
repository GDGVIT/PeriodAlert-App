package com.dscvit.periodsapp.ui.requests


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.adapter.RequestListAdapter
import com.dscvit.periodsapp.model.requests.Request
import com.dscvit.periodsapp.repository.AppRepository
import com.dscvit.periodsapp.utils.hide
import com.dscvit.periodsapp.utils.show
import kotlinx.android.synthetic.main.fragment_requests.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class RequestsFragment : Fragment() {

    private val repo by inject<AppRepository>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestListAdapter = RequestListAdapter()
        requestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestListAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        repo.getAllRequests().observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                noRequestTextView.show()
            } else {
                noRequestTextView.hide()
            }
            requestListAdapter.updateRequests(it)
        })

    }
}
