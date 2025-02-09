package com.example.gamehub.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.Resource
import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.domain.model.GamesModel
import com.example.core.domain.repository.IGameHubRepository
import com.example.core.domain.usecase.GameHubUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: IGameHubRepository,
    private val gamesHubUseCase: GameHubUseCase
) : ViewModel() {

    private val _gameDetail = MutableStateFlow<Resource<GameDetailResponse>>(Resource.Loading())
    val gameDetail: StateFlow<Resource<GameDetailResponse>> = _gameDetail.asStateFlow()

    fun getGameDetail(gameId: Int) {
        viewModelScope.launch {
            repository.getGameDetail(gameId).collect { result ->
                _gameDetail.value = result
            }
        }
    }

    fun setFavoriteGames(tourism: GamesModel, newStatus: Boolean) =
        gamesHubUseCase.setFavoriteGames(tourism, newStatus)

    fun setNewFavoriteGames(tourism: GamesModel, newStatus: Boolean) =
        gamesHubUseCase.setNewFavoriteGames(tourism, newStatus)

    fun updateFavoriteGame(game: GamesModel, newStatus: Boolean) {
        val updatedGame = game.copy(isFavorite = newStatus)
        viewModelScope.launch {
            gamesHubUseCase.updateFavoriteGame(updatedGame)
        }
    }


}