package com.example.stickerapp.domain.usecases

import com.example.stickerapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.map

class GetTeamUseCase(private val teamRepository: TeamRepository) {

    suspend operator fun invoke(position: Int) = teamRepository.getTeam(position)

    fun getTeamStickers(position: Int) = teamRepository.getTeamStickers(position).map {
        it.copy(stickers = it.toSetOfStickers().toList())
    }
}