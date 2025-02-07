package com.example.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_releases_games")
data class NewReleasesGames(

    @PrimaryKey
    val gameId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "background_image")
    val backgroundImage: String,

    @ColumnInfo(name = "released")
    val released: String,

    @ColumnInfo(name = "genres")
    val genres: String,

    @ColumnInfo(name = "parent_platforms")
    val parentPlatforms: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false

)