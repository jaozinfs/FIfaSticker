package com.example.stickerapp.presenter.sticker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stickerapp.domain.usecases.AddTeamStickersUseCase
import com.example.stickerapp.domain.Team
import com.example.stickerapp.domain.usecases.GetTeamUseCase
import com.example.stickerapp.domain.usecases.UpdateTeamStickersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StickersViewModel(
    private val updateTeamUseCase: UpdateTeamStickersUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val addTeamStickersUseCase: AddTeamStickersUseCase
) : ViewModel() {
    private lateinit var team: Team
    private var filterLostCards: Boolean = false

    private val _state = MutableStateFlow<StickersState>(StickersState.IDLE)
    val state = _state.asStateFlow()

    fun event(stickersEvents: StickersEvents) {
        when (stickersEvents) {
            is StickersEvents.GetStickers -> getStickers(stickersEvents.position)
            is StickersEvents.GetTeam -> getTeam(stickersEvents.position)
            is StickersEvents.UpdateTeam -> updateTeam(team, stickersEvents.sticker)
            is StickersEvents.UpdateImage -> updateImage(team, stickersEvents.images)
            StickersEvents.FilterLostCards -> {
                updateFilter()
                updateStickers()
            }
        }
    }

    private fun updateImage(team: Team, images: Pair<Int, String>) = viewModelScope.launch {
        addTeamStickersUseCase.invoke(team, images)
    }

    private fun updateFilter() {
        filterLostCards = !filterLostCards
    }

    private fun updateStickers() {
        _state.value =
            if (filterLostCards)
                StickersState.FilterStickers(team.copy(stickers = team.stickers.filter { it.second.not() }))
            else
                StickersState.FetchStickers(team.copy(stickers = team.stickers))
    }

    private fun updateTeam(team: Team, sticker: Pair<Int, Boolean>) = viewModelScope.launch {
        updateTeamUseCase.invoke(team, sticker)
    }

    private fun getTeam(position: Int) = viewModelScope.launch {
        _state.value = StickersState.FetchTeam(getTeamUseCase.invoke(position))
    }

    private fun getStickers(position: Int) = viewModelScope.launch {
        getTeamUseCase.getTeamStickers(position).collectLatest {
            team = it
            updateStickers()
        }
    }

    sealed class StickersState {
        object IDLE : StickersState()
        data class FetchTeam(val team: Team) : StickersState()
        data class FetchStickers(val team: Team) : StickersState()
        data class FilterStickers(val team: Team) : StickersState()
    }

    sealed class StickersEvents {
        data class GetStickers(val position: Int) : StickersEvents()
        data class GetTeam(val position: Int) : StickersEvents()
        data class UpdateTeam(val sticker: Pair<Int, Boolean>) : StickersEvents()
        data class UpdateImage(val images: Pair<Int, String>) : StickersEvents()
        object FilterLostCards : StickersEvents()
    }
}