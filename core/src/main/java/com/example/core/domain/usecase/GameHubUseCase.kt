package com.example.core.domain.usecase

import com.example.core.data.source.Resource
import com.example.core.data.source.local.entity.NewReleasesGames
import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.domain.model.GamesModel
import kotlinx.coroutines.flow.Flow

interface GameHubUseCase {

    fun getAllGames(): Flow<Resource<List<GamesModel>>>

    fun getNewReleasesGame(): Flow<Resource<List<GamesModel>>>

    fun getGameDetail(gameId: Int): Flow<Resource<GameDetailResponse>>

    fun getFavoriteTourism(): Flow<List<GamesModel>>

    fun setFavoriteGames(game: GamesModel, state: Boolean)
}