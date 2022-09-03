package com.example.stickerapp.domain.usecases

import com.example.stickerapp.domain.Team
import com.example.stickerapp.domain.repository.TeamRepository

class GetLostStickersText(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(team: Team?) = repository.getAllTeams().filter {
        if (team != null)
            it.name == team.name
        else
            true
    }.map {
        it to it.toSetOfStickers()
    }.map {
        it.first to it.second.filter { sticker ->
            sticker.second.not()
        }
    }.joinToString(separator = ", \n") {
        "Time: ${it.first.name} \n " + getTeamString(it.first, it.second)
    }

    private fun getTeamString(team: Team, stickers: List<Pair<Int, Boolean>>): String {
        var string = ""
        stickers.forEachIndexed { index, sticker ->
            string += " ${team.prefix} ${sticker.first} " + if (index == stickers.size - 1) "" else "\n"
        }
        return string
    }
}