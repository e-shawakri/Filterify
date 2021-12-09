package com.example.filterify.utilities

import android.app.Application
import com.example.filterify.di.repoModule
import com.example.filterify.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repoModule, viewModelModule))
        }
    }
}