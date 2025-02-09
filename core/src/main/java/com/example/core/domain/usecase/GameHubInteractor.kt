package com.example.core.domain.usecase

import com.example.core.domain.model.GamesModel
import com.example.core.domain.repository.IGameHubRepository
import kotlinx.coroutines.flow.Flow

class GameHubInteractor(private val gameHubRepository: IGameHubRepository): GameHubUseCase {
    override fun getAllGames() = gameHubRepository.getAllGames()

    override fun getNewReleasesGame() = gameHubRepository.getNewReleasesGame()

    override fun getGameDetail(gameId: Int) = gameHubRepository.getGameDetail(gameId)

    override fun getFavoriteGame() = gameHubRepository.getFavoriteGames()
    override fun getNewFavorite() = gameHubRepository.getNewFavoriteGames()

    override fun setFavoriteGames(game: GamesModel, state: Boolean) = gameHubRepository.setFavoriteGames(game, state)

    override suspend fun updateFavoriteGame(game: GamesModel) = gameHubRepository.updateFavoriteGame(game)

    override fun setNewFavoriteGames(game: GamesModel, state: Boolean) = gameHubRepository.setNewFavoriteGames(game, state)

    override fun searchGames(query: String) = gameHubRepository.searchGames(query)

}