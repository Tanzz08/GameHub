package com.example.core.utils

import com.example.core.data.source.local.entity.GamesListEntity
import com.example.core.data.source.local.entity.NewReleasesGames
import com.example.core.data.source.remote.response.GameDetailResponse
import com.example.core.data.source.remote.response.ResultsItem
import com.example.core.domain.model.GamesModel

object DataMapper {
    fun mapResponsesToEntities(input: List<ResultsItem>): List<GamesListEntity> {
        val gamesList = ArrayList<GamesListEntity>()
        input.map {
            val games = GamesListEntity(
                gameId = it.id.toString(),
                name = it.name ,
                backgroundImage = it.backgroundImage ?: "No data",
                released = it.released,
                genres = it.genres.joinToString(separator = ", ") { it.name } ?: "No Data",
                parentPlatforms = it.parentPlatforms.joinToString { it.platform.name } ?: "No Data",
                isFavorite = false
            )
            gamesList.add(games)
        }
        return gamesList
    }


    fun mapResponsesToEntitiesNew(input: List<ResultsItem>): List<NewReleasesGames> {
        val gamesList = ArrayList<NewReleasesGames>()
        input.map {
            val games = NewReleasesGames(
                gameId = it.id.toString(),
                name = it.name,
                backgroundImage = it.backgroundImage,
                released = it.released,
                genres = it.genres.joinToString { it.name },
                parentPlatforms = it.parentPlatforms.joinToString { it.platform.name },
                isFavorite = false
            )
            gamesList.add(games)
        }
        return gamesList
    }

    fun mapResponsesToDomain(input: List<ResultsItem>): List<GamesModel> =
        input.map {
            GamesModel(
                gameId = it.id.toString(),
                name = it.name,
                backgroundImage = it.backgroundImage ?: "",
                released = it.released ?: "",
                genres = it.genres.toString() ?: "",
                parentPlatforms = it.parentPlatforms.map { it.platform.name }.toString(),
                isFavorite = false,

            )
        } ?: emptyList()

    fun mapResponseDetailToDomain(input: GameDetailResponse): GamesModel =
        GamesModel(
            gameId = input.id.toString(),
            name = input.name,
            backgroundImage = input.backgroundImage ?: "",
            released = input.released ?: "",
            genres = input.genres.joinToString { it.name } ?: "",
            description = input.description ?: "",
            parentPlatforms = input.parentPlatforms.joinToString { it.platform.name } ?: "",
            isFavorite = false
        )

    fun mapEntitiesToDomain(input: List<GamesListEntity>): List<GamesModel> =
        input.map {
            GamesModel(
                gameId = it.gameId,
                name = it.name.toString(),
                backgroundImage = it.backgroundImage,
                released = it.released,
                genres = it.genres.toString(),
                parentPlatforms = it.parentPlatforms,
                isFavorite = it.isFavorite,

                )
        }

    fun mapNewEntitiesToDomain(input: List<NewReleasesGames>): List<GamesModel> =
        input.map {
            GamesModel(
                gameId = it.gameId,
                name = it.name,
                backgroundImage = it.backgroundImage,
                released = it.released,
                genres = it.genres,
                parentPlatforms = it.parentPlatforms,
                isFavorite = it.isFavorite,

                )
        }


    fun mapEntitiesToDomainNew(input: List<NewReleasesGames>): List<GamesModel> =
        input.map {
            GamesModel(
                gameId = it.gameId,
                name = it.name,
                backgroundImage = it.backgroundImage,
                released = it.released,
                genres = it.genres,
                parentPlatforms = it.parentPlatforms,
                isFavorite = it.isFavorite,

                )
        }

    fun mapDomainToEntity(input: GamesModel) = GamesListEntity(
        gameId = input.gameId ?: "",
        name = input.name,
        backgroundImage = input.backgroundImage ?: "",
        released = input.released ?: "",
        genres = input.genres,
        parentPlatforms = input.parentPlatforms ?: "",
        isFavorite = input.isFavorite
    )

    fun mapDomainToNewEntity(input: GamesModel) = NewReleasesGames(
        gameId = input.gameId ?: "",
        name = input.name ?: "",
        backgroundImage = input.backgroundImage ?: "",
        released = input.released ?: "",
        genres = input.genres ?: "",
        parentPlatforms = input.parentPlatforms ?: "",
        isFavorite = input.isFavorite
    )


}