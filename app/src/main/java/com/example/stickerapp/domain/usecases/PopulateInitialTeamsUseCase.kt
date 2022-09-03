package com.example.stickerapp.domain.usecases

import com.example.stickerapp.domain.TeamFactory
import com.example.stickerapp.domain.repository.TeamRepository

class PopulateInitialTeamsUseCase(
    private val repository: TeamRepository
) {
    suspend operator fun invoke() {
        repository.addTeams(TeamFactory.getTeams())
    }
}