package com.example.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GameHubUseCase

class FavoriteViewModel(gameHubUseCase: GameHubUseCase) : ViewModel() {
    val favoriteGames = gameHubUseCase.getFavoriteGame().asLiveData()
}