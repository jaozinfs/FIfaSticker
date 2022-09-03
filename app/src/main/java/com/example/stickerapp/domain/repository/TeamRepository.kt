package com.example.stickerapp.domain.repository

import com.example.stickerapp.domain.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun updateTeamStickers(team: Team)
    suspend fun addImage(team: Team)
    suspend fun getTeam(position: Int): Team
    fun getTeamStickers(position: Int): Flow<Team>
    suspend fun addTeams(teams: List<Team>)
    fun getTeams(): Flow<List<Team>>
    suspend fun getAllTeams(): List<Team>
}