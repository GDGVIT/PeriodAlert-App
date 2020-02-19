package com.dscvit.periodsapp.ui.auth

import androidx.lifecycle.ViewModel
import com.dscvit.periodsapp.model.login.LoginRequest
import com.dscvit.periodsapp.model.registernotification.RegisterNotificationRequest
import com.dscvit.periodsapp.model.signup.SignupRequest
import com.dscvit.periodsapp.repository.AppRepository

class AuthViewModel(private val repo: AppRepository) : ViewModel() {

    fun signUpUser(signupRequest: SignupRequest) = repo.signUpUser(signupRequest)

    fun signInUser(loginRequest: LoginRequest) = repo.signInUser(loginRequest)

    fun registerDevice(registerNotificationRequest: RegisterNotificationRequest) =
        repo.registerDevice(registerNotificationRequest)

    fun updateDeviceDetails(registerNotificationRequest: RegisterNotificationRequest) =
        repo.updateDeviceDetails(registerNotificationRequest)

}