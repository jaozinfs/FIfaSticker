package com.example.stickerapp.data.repository

import com.example.stickerapp.data.database.stickers.TeamEntity
import com.example.stickerapp.data.database.stickers.TeamDao
import com.example.stickerapp.domain.Team
import com.example.stickerapp.domain.repository.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TeamRepositoryImpl(private val teamDao: TeamDao) : TeamRepository {

    override suspend fun updateTeamStickers(team: Team) = withContext(Dispatchers.IO) {
        val entity = teamDao.getTeam(team.name)
        teamDao.updateTeam(entity.id, team.stickers)
    }

    override suspend fun addImage(team: Team) {
        val entity = teamDao.getTeam(team.name)
        teamDao.addImage(entity.id, team.images)
    }

    override suspend fun getTeam(position: Int) = teamDao.getTeam(position).toDomain()

    override fun getTeamStickers(position: Int) = teamDao.getTeamStickers(position).map {
        it.toDomain()
    }

    override suspend fun addTeams(teams: List<Team>) {
        val teamsEntity = teams.map {
            TeamEntity(it.name, it.prefix, it.position, it.stickers, it.images)
        }
        teamDao.addTeam(teamsEntity)
    }

    override fun getTeams() = teamDao.getTeams().flowOn(Dispatchers.IO).map {
        it.toDomain()
    }

    override suspend fun getAllTeams() = teamDao.getAllTeams().toDomain()

    private fun List<TeamEntity>.toDomain() = map { it.toDomain() }
    private fun TeamEntity.toDomain() = Team(name, prefix, position, stickers, images)
}