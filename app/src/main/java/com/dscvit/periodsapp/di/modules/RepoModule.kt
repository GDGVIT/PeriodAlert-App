package com.dscvit.periodsapp.di.modules

import com.dscvit.periodsapp.repository.AppRepository
import org.koin.dsl.module

val repoModule = module {
    factory { AppRepository(get(), get()) }
}