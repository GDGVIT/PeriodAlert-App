package com.dscvit.periodsapp.di.modules

import com.dscvit.periodsapp.websocket.ChatWsListener
import org.koin.dsl.module

val wsModule = module {
    factory { ChatWsListener(get(), get()) }
}