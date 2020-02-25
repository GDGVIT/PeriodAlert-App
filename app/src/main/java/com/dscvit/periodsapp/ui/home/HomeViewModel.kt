package com.dscvit.periodsapp.ui.home

import androidx.lifecycle.ViewModel
import com.dscvit.periodsapp.model.sendalert.SendAlertRequest
import com.dscvit.periodsapp.repository.AppRepository

class HomeViewModel (private val repo: AppRepository) : ViewModel() {

    fun sendAlert(sendAlertRequest: SendAlertRequest) = repo.sendAlert(sendAlertRequest)

    fun logOut() = repo.logOut()

}