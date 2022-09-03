package com.example.stickerapp.domain.usecases

import com.example.stickerapp.domain.Team
import com.example.stickerapp.domain.repository.TeamRepository

class UpdateTeamStickersUseCase(
    private val repository: TeamRepository
) {
    suspend operator fun invoke(team: Team, sticker: Pair<Int, Boolean>) {
        val stickers = team.stickers.toMutableList()
        stickers.remove(sticker)
        stickers.add(sticker.first to !sticker.second)

        repository.updateTeamStickers(team.copy(stickers = stickers))
    }
}