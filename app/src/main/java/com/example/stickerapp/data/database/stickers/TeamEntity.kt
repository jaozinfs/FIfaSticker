package com.example.stickerapp.data.database.stickers

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Entity(tableName = "TeamEntity")
data class TeamEntity(
    val name: String,
    val prefix: String,
    val position: Int,
    val stickers: List<Pair<Int, Boolean>>,
    val images: List<Pair<Int, String>>
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

object TeamEntityConverter {
    @TypeConverter
    fun fromString(value: String?): List<Pair<Int, Boolean>> {
        val listType: Type = object : TypeToken<List<Pair<Int, Boolean>>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Pair<Int, Boolean>>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}

object ImagesConverter {
    @TypeConverter
    fun fromString(value: String?): List<Pair<Int, String>> {
        val listType: Type = object : TypeToken<List<Pair<Int, String>>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Pair<Int, String>>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}