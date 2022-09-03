package com.example.stickerapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stickerapp.data.database.stickers.StickerDb
import com.example.stickerapp.domain.usecases.PopulateInitialTeamsUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
@DelicateCoroutinesApi
object RoomDI : KoinComponent {
    private val populateInitialTeamsUseCase: PopulateInitialTeamsUseCase by inject()

    fun initializeDb(context: Context) =
        Room.databaseBuilder(context, StickerDb::class.java, "Sticker")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    GlobalScope.launch(Dispatchers.IO) { populateInitialTeamsUseCase.invoke() }
                }
            })
            .build()
}