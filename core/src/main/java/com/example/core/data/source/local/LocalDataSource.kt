package com.example.core.data.source.local

import android.util.Log
import com.example.core.data.source.local.entity.GamesListEntity
import com.example.core.data.source.local.entity.NewReleasesGames
import com.example.core.data.source.local.room.GameHubDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSource(private val gameHubDao: GameHubDao)  {

    fun getAllGames(): Flow<List<GamesListEntity>> = gameHubDao.getAllGames()

    fun getNewReleasesGame(): Flow<List<NewReleasesGames>> = gameHubDao.getNewReleasesGame()

    fun getFavoriteGame(): Flow<List<GamesListEntity>> = gameHubDao.getFavoriteGames().map {
        it.forEach { game ->
            Log.d("LocalDataSource", "Game: ${game.name}, isFavorite: ${game.isFavorite}")
        }
        it
    }

    fun getNewFavoriteGame(): Flow<List<NewReleasesGames>> = gameHubDao.getNewFavoriteGames().map {
        it.forEach { game ->
            Log.d("LocalDataSource", "Game: ${game.name}, isFavorite: ${game.isFavorite}")
        }
        it
    }

    suspend fun insertGames(games: List<GamesListEntity>) = gameHubDao.insertGames(games)

    suspend fun insertGamesNew(gameList: List<NewReleasesGames>) = gameHubDao.insertGamesNew(gameList)

    fun updateFavoriteGame(game: GamesListEntity) = gameHubDao.updateFavoriteGame(game)

    fun setFavoriteGame(game: GamesListEntity, newState: Boolean) {
        game.isFavorite = newState
        gameHubDao.updateFavoriteGame(game)
    }

    fun setNewFavoriteGame(game: NewReleasesGames, newState: Boolean) {
        game.isFavorite = newState
        gameHubDao.updateNewFavoriteGame(game)
    }
}