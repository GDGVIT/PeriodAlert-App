package com.dscvit.periodsapp

import android.app.Application
import android.content.Context
import com.dscvit.periodsapp.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appComponent)
        }
    }

    companion object {
        lateinit var context: Context
    }
}
