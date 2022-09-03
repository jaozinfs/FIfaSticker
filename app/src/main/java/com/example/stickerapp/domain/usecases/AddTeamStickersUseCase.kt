package com.example.stickerapp.domain.usecases

import com.example.stickerapp.domain.Team
import com.example.stickerapp.domain.repository.TeamRepository

class AddTeamStickersUseCase(
    private val repository: TeamRepository
) {
    suspend operator fun invoke(team: Team, image: Pair<Int, String>) {
        val images = team.images.toMutableList()
        images.removeAll { image.first == it.first }
        images.add(image)

        repository.addImage(team.copy(images = images))
    }
}