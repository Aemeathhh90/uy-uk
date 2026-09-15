package com.kakaanime.app.ui.library

import com.kakaanime.app.ui.home.HomeAnimeUi

/** UI-only Library state. Persistence and repository logic stay outside the UI repository. */
data class LibraryUiState(
    val favorites: List<HomeAnimeUi> = emptyList(),
    val searchQuery: String = "",
)
