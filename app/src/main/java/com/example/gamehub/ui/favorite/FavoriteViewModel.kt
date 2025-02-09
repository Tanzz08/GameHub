package com.example.gamehub.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.model.GamesModel
import com.example.core.domain.model.ListItem
import com.example.core.domain.usecase.GameHubUseCase
import kotlinx.coroutines.flow.combine

class FavoriteViewModel(gameHubUseCase: GameHubUseCase) : ViewModel() {
    val favoriteGames = gameHubUseCase.getFavoriteGame().asLiveData()
    val newReleasesGames = gameHubUseCase.getNewFavorite().asLiveData()

    private val favoriteGamesFlow = gameHubUseCase.getFavoriteGame()
    private val newReleasesGamesFlow = gameHubUseCase.getNewFavorite()

    val combinedListItems: LiveData<List<ListItem>> = combine(favoriteGamesFlow, newReleasesGamesFlow) { favoriteGames, newReleasesGames ->
        val favoriteListItems = favoriteGames.map { ListItem.FavoriteGame(it) }
        val newReleasesListItems = newReleasesGames.map { ListItem.NewReleaseGame(it) }
        favoriteListItems + newReleasesListItems
    }.asLiveData()
}