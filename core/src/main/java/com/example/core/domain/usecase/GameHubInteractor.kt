package com.example.core.domain.usecase

import com.example.core.data.source.Resource
import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.domain.model.GamesModel
import com.example.core.domain.repository.IGameHubRepository
import kotlinx.coroutines.flow.Flow

class GameHubInteractor(private val gameHubRepository: IGameHubRepository): GameHubUseCase {
    override fun getAllGames() = gameHubRepository.getAllGames()

    override fun getNewReleasesGame() = gameHubRepository.getNewReleasesGame()

    override fun getGameDetail(gameId: Int) = gameHubRepository.getGameDetail(gameId)

    override fun getFavoriteTourism() = gameHubRepository.getFavoriteGames()

    override fun setFavoriteGames(game: GamesModel, state: Boolean) = gameHubRepository.setFavoriteTourism(game, state)

}