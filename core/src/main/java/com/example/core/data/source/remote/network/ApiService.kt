package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.data.source.remote.response.ListGamesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("games")
    suspend fun getListOfGames(
        @Query("key") apiKey: String,
        @Query("ordering") odering: String = "-added"
    ) : ListGamesResponse

    @GET("games")
    suspend fun getNewReleasesGame(
        @Query("key") apiKey: String,
        @Query("ordering") odering: String = "-released",
        @Query("dates") dates: String = "2025-01-01,2025-02-01"
    ) : ListGamesResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ) : GameDetailResponse

    @GET("games")
    suspend fun getSearchGame(
        @Query("key") apiKey: String,
        @Query("search") search: String,
    ) : ListGamesResponse
}