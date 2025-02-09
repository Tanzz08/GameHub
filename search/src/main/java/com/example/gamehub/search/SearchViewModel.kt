package com.example.gamehub.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GameHubUseCase

class SearchViewModel(private val gameHubUseCase: GameHubUseCase) : ViewModel() {
    fun searchGames(query: String) = gameHubUseCase.searchGames(query).asLiveData()
}