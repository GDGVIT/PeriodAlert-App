package com.dscvit.periodsapp.di

import com.dscvit.periodsapp.di.modules.*

val appComponent =
    listOf(apiModule, repoModule, viewModelModule, roomModule, wsModule)