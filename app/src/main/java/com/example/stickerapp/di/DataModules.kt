package com.example.stickerapp.di

import com.example.stickerapp.data.database.RoomDI
import com.example.stickerapp.data.database.stickers.StickerDb
import com.example.stickerapp.data.repository.TeamRepositoryImpl
import com.example.stickerapp.domain.repository.TeamRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModules = module {
    single { RoomDI.initializeDb(androidContext()) }
    single { get<StickerDb>().teamDao }
    single<TeamRepository> { TeamRepositoryImpl(get()) }
}