package com.kakaanime.app.ui.detail

/** Presentation-only models. Provider/network/data models stay outside the UI repository. */
data class AnimeDetailUi(
    val id: String,
    val title: String,
    val description: String = "",
    val genre: String = "",
    val year: String = "-",
    val type: String = "TV",
    val status: String = "Ongoing",
    val studio: String = "-",
    val season: String = "Season 1",
    val rating: String = "-",
    val posterUrl: String? = null,
)

data class EpisodeUi(
    val number: Int,
    val title: String = "Episode $number",
    val thumbnailUrl: String? = null,
    val isNew: Boolean = false,
    val isWatched: Boolean = false,
    val isLocked: Boolean = true,
)

data class SeasonUi(
    val id: String,
    val label: String,
)

enum class DetailTab { INFO, EPISODES }
en
enum class EpisodeFilter(val label: String) {
    ALL("Semua"),
    NEW("Terbaru"),
    UNWATCHED("Belum Ditonton"),
}

enum class EpisodeViewMode { LIST, GRID }
