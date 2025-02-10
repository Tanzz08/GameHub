package com.example.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.data.source.local.entity.GamesListEntity
import com.example.core.data.source.local.entity.NewReleasesGames
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHubDao {

    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<GamesListEntity>>

    @Query("SELECT * FROM new_releases_games")
    fun getNewReleasesGame(): Flow<List<NewReleasesGames>>

    @Query("SELECT * FROM games where isFavorite = 1")
    fun getFavoriteGames(): Flow<List<GamesListEntity>>

    @Query("SELECT * FROM new_releases_games where isFavorite = 1")
    fun getNewFavoriteGames(): Flow<List<NewReleasesGames>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGames(game: List<GamesListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGamesNew(game: List<NewReleasesGames>)

    @Update
    fun updateFavoriteGame(game: GamesListEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM games WHERE gameId = :gameId)")
    suspend fun isGameExists(gameId: String): Boolean

    @Update
    fun updateNewFavoriteGame(game: NewReleasesGames)

}