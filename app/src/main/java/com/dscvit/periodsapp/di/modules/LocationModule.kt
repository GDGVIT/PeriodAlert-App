package com.dscvit.periodsapp.di.modules

import com.dscvit.periodsapp.utils.LocationHelper
import org.koin.dsl.module

val locationModule = module {
    factory { LocationHelper(get()) }
}