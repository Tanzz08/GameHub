package com.example.gamehub.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GameHubUseCase

class HomeViewModel(gameHubUseCase: GameHubUseCase) : ViewModel() {

    val games = gameHubUseCase.getAllGames().asLiveData()

    val newReleasesGame = gameHubUseCase.getNewReleasesGame().asLiveData()
}