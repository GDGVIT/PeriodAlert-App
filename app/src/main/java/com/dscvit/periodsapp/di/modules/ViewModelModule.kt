package com.dscvit.periodsapp.di.modules

import com.dscvit.periodsapp.ui.auth.AuthViewModel
import com.dscvit.periodsapp.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}