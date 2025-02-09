package com.example.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.source.local.entity.GamesListEntity
import com.example.core.data.source.local.entity.NewReleasesGames

@Database(entities = [GamesListEntity::class, NewReleasesGames::class], version = 2, exportSchema = false)
abstract class GameHubDatabase : RoomDatabase() {
    abstract fun gameHubDao(): GameHubDao

}