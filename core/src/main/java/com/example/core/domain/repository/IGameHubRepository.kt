package com.example.core.domain.repository

import com.example.core.data.source.Resource
import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.domain.model.GamesModel
import kotlinx.coroutines.flow.Flow

interface IGameHubRepository {

    fun getAllGames(): Flow<Resource<List<GamesModel>>>

    fun getNewReleasesGame(): Flow<Resource<List<GamesModel>>>

    fun getGameDetail(gameId: Int): Flow<Resource<GameDetailResponse>>

    fun getFavoriteGames(): Flow<List<GamesModel>>

    fun setFavoriteTourism(game: GamesModel, state: Boolean)
}