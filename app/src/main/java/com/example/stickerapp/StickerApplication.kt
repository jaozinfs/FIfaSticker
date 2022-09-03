package com.example.stickerapp

import android.app.Application
import com.example.stickerapp.di.dataModules
import com.example.stickerapp.di.domainModules
import com.example.stickerapp.di.presenterModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin

@KoinApiExtension
class StickerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@StickerApplication)
            modules(dataModules, domainModules, presenterModules)
        }
    }
}