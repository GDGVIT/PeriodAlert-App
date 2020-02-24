package com.dscvit.periodsapp.di.modules

import androidx.room.Room
import com.dscvit.periodsapp.db.ChatsDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidApplication(), ChatsDatabase::class.java, "chatsdb")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ChatsDatabase>().chatsDao() }
}