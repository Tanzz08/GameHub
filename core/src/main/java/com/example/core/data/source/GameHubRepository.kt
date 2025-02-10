package com.example.core.data.source

import android.util.Log
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.data.source.remote.response.ResultsItem
import com.example.core.domain.model.GamesModel
import com.example.core.domain.repository.IGameHubRepository
import com.example.core.utils.AppExecutors
import com.example.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class GameHubRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IGameHubRepository {
    override fun getAllGames(): Flow<Resource<List<GamesModel>>> =
        object : NetworkBoundResource<List<GamesModel>, List<ResultsItem>>() {

            override fun loadFromDB(): Flow<List<GamesModel>> {
                return localDataSource.getAllGames().map { DataMapper.mapEntitiesToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
                remoteDataSource.getListOfGames()


            override suspend fun saveCallResult(data: List<ResultsItem>) {
                val gamesList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertGames(gamesList)
            }

            override fun shouldFetch(data: List<GamesModel>?): Boolean =
                data.isNullOrEmpty()

        }.asFlow()

    override fun getNewReleasesGame(): Flow<Resource<List<GamesModel>>> =
        object : NetworkBoundResource<List<GamesModel>, List<ResultsItem>>() {
            override fun loadFromDB(): Flow<List<GamesModel>> {
                return localDataSource.getNewReleasesGame().map { DataMapper.mapEntitiesToDomainNew(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
                remoteDataSource.getNewReleasesGame()

            override suspend fun saveCallResult(data: List<ResultsItem>) {
                val gameList = DataMapper.mapResponsesToEntitiesNew(data)
                localDataSource.insertGamesNew(gameList)
            }

            override fun shouldFetch(data: List<GamesModel>?): Boolean =
                data.isNullOrEmpty()

        }.asFlow()

    override fun searchGames(query: String): Flow<Resource<List<GamesModel>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remoteDataSource.searchGames(query).first()) {
                is ApiResponse.Success -> {
                    val gameList = DataMapper.mapResponsesToDomain(response.data)
                    val gameEntity = DataMapper.mapResponsesToEntities(response.data)
                    appExecutors.diskIO().execute {
                        runBlocking {
                            localDataSource.insertGames(gameEntity)
                        }
                    }
                    emit(Resource.Success(gameList))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("Data is empty"))
                }
            }
        }

    override fun getGameDetail(gameId: Int): Flow<Resource<GamesModel>> {
        return flow {
            emit(Resource.Loading())

            val response = remoteDataSource.getGameDetail(gameId).first()

            when (response) {
                is ApiResponse.Success -> {
                    val gameDetail = DataMapper.mapResponseDetailToDomain(response.data)
                    emit(Resource.Success(gameDetail))
                }

                is ApiResponse.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }

                is ApiResponse.Empty -> {
                    emit(Resource.Error("Data is empty"))
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message ?: "An error occurred"))
            Log.e("GameHubRepository", "Error fetching game detail: ${e.message}", e)
        }.flowOn(Dispatchers.IO)
    }


    override fun getFavoriteGames(): Flow<List<GamesModel>> {
        return localDataSource.getFavoriteGame().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getNewFavoriteGames(): Flow<List<GamesModel>> {
        return localDataSource.getNewFavoriteGame().map {
            DataMapper.mapNewEntitiesToDomain(it)
        }
    }

    override suspend fun updateFavoriteGame(game: GamesModel) {
        val gameEntity = DataMapper.mapDomainToEntity(game)
        appExecutors.diskIO().execute{
            localDataSource.updateFavoriteGame(gameEntity)
        }
    }

    override fun setFavoriteGames(game: GamesModel, state: Boolean) {
        val gameEntity = DataMapper.mapDomainToEntity(game)
        Log.d("GameHubRepository", "Setting favorite for ${game.name} to $state")
        appExecutors.diskIO().execute {
            Log.d("GameHubRepository", "Before update: ${gameEntity.name}, isFavorite: ${gameEntity.isFavorite}")
            localDataSource.setFavoriteGame(gameEntity, state)
            Log.d("GameHubRepository", "After update: ${gameEntity.name}, isFavorite: ${gameEntity.isFavorite}")
        }
    }

    override fun setNewFavoriteGames(game: GamesModel, state: Boolean) {
        val gameEntity = DataMapper.mapDomainToNewEntity(game)
        Log.d("GameHubRepository", "Setting favorite for ${game.name} to $state")
        appExecutors.diskIO().execute {
            Log.d("GameHubRepository", "Before update: ${gameEntity.name}, isFavorite: ${gameEntity.isFavorite}")
            localDataSource.setNewFavoriteGame(gameEntity, state)
            Log.d("GameHubRepository", "After update: ${gameEntity.name}, isFavorite: ${gameEntity.isFavorite}")
        }
    }
}