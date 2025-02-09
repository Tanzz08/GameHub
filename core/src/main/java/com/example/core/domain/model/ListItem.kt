package com.example.core.domain.model

sealed class ListItem {
        data class FavoriteGame(val game: GamesModel) : ListItem()
        data class NewReleaseGame(val game: GamesModel) : ListItem()
}