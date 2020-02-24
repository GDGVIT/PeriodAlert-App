package com.dscvit.periodsapp.repository

import com.dscvit.periodsapp.model.login.LoginRequest
import com.dscvit.periodsapp.model.registernotification.RegisterNotificationRequest
import com.dscvit.periodsapp.model.sendalert.SendAlertRequest
import com.dscvit.periodsapp.model.signup.SignupRequest
import com.dscvit.periodsapp.network.ApiClient
import com.dscvit.periodsapp.network.ApiInterface
import com.dscvit.periodsapp.network.BaseApiClient

class AppRepository(private val apiClient: ApiClient) : BaseRepo() {
    fun signUpUser(signupRequest: SignupRequest) = makeRequest {
        apiClient.signUpUser(signupRequest)
    }

    fun signInUser(loginRequest: LoginRequest) = makeRequest {
        apiClient.signInUser(loginRequest)
    }

    fun registerDevice(registerNotificationRequest: RegisterNotificationRequest) = makeRequest {
        apiClient.registerDevice(registerNotificationRequest)
    }

    fun updateDeviceDetails(registerNotificationRequest: RegisterNotificationRequest) =
        makeRequest {
            apiClient.updateDeviceDetails(registerNotificationRequest)
        }

    fun sendAlert(sendAlertRequest: SendAlertRequest) = makeRequest {
        apiClient.sendAlert(sendAlertRequest)
    }

    fun logOut() = makeRequest {
        apiClient.logOut()
    }

    fun viewChatRooms() = makeRequest {
        apiClient.viewChatRooms()
    }

    fun getMessages(chatRoomId: Int) = makeRequest {
        apiClient.getMessages(chatRoomId)
    }
}