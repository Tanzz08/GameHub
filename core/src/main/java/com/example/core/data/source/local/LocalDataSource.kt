package com.example.core.data.source.local

import com.example.core.data.source.local.entity.GamesListEntity
import com.example.core.data.source.local.entity.NewReleasesGames
import com.example.core.data.source.local.room.GameHubDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val gameHubDao: GameHubDao)  {

    fun getAllGames(): Flow<List<GamesListEntity>> = gameHubDao.getAllGames()

    fun getNewReleasesGame(): Flow<List<NewReleasesGames>> = gameHubDao.getNewReleasesGame()

    fun getFavoriteGame(): Flow<List<GamesListEntity>> = gameHubDao.getFavoriteGames()

    suspend fun insertGames(gameList: List<GamesListEntity>) = gameHubDao.insertGames(gameList)

    suspend fun insertGamesNew(gameList: List<NewReleasesGames>) = gameHubDao.insertGamesNew(gameList)

    fun setFavoriteGame(game: GamesListEntity, newState: Boolean) {
        game.isFavorite = newState
        gameHubDao.updateFavoriteGame(game)
    }
}