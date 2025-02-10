package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamesModel (
    val gameId: String?,
    val name: String?,
    val backgroundImage: String?,
    val released: String?,
    val genres: String?,
    val parentPlatforms: String?,
    val description: String? = null,
    val isFavorite: Boolean
): Parcelable