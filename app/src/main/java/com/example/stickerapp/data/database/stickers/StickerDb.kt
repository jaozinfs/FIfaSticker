package com.example.stickerapp.data.database.stickers

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TeamEntity::class], version = 1)
@TypeConverters(value = [TeamEntityConverter::class, ImagesConverter::class])
abstract class StickerDb() : RoomDatabase() {
    abstract val teamDao: TeamDao
}