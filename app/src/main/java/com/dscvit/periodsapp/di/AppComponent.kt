package com.dscvit.periodsapp.di

import com.dscvit.periodsapp.di.modules.apiModule
import com.dscvit.periodsapp.di.modules.repoModule
import com.dscvit.periodsapp.di.modules.viewModelModule

val appComponent = listOf(apiModule, repoModule, viewModelModule)