package com.example.stickerapp.domain.usecases

import com.example.stickerapp.domain.repository.TeamRepository

class GetAllTeamsUseCase(
    private val repository: TeamRepository
) {

    operator fun invoke() = repository.getTeams()
}