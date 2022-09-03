package com.example.stickerapp.data.database.stickers

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Insert
    suspend fun addTeam(team: TeamEntity)

    @Insert
    suspend fun addTeam(team: List<TeamEntity>)

    @Query("UPDATE TeamEntity SET stickers = :stickers WHERE id = :id")
    @TypeConverters(TeamEntityConverter::class, ImagesConverter::class)
    suspend fun updateTeam(id: Int, stickers: List<Pair<Int, Boolean>>)

    @Query("SELECT * from TeamEntity WHERE name = :teamName")
    suspend fun getTeam(teamName: String): TeamEntity

    @Query("SELECT * FROM TeamEntity")
    fun getTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM TeamEntity WHERE position = :position")
    suspend fun getTeam(position: Int): TeamEntity

    @Query("SELECT * FROM TeamEntity WHERE position = :position")
    fun getTeamStickers(position: Int): Flow<TeamEntity>

    @Query("SELECT * FROM TeamEntity")
    suspend fun getAllTeams(): List<TeamEntity>

    @Query("UPDATE TeamEntity SET images = :images WHERE id = :id")
    @TypeConverters(TeamEntityConverter::class, ImagesConverter::class)
    suspend fun addImage(id: Int, images: List<Pair<Int, String>>)
}