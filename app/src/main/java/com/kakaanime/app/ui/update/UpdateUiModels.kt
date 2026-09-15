package com.kakaanime.app.ui.update

/** UI-only update information. Version checks and download/install logic stay outside the UI repository. */
data class UpdateUiState(
    val isUpdateAvailable: Boolean = false,
    val versionLabel: String = "",
    val changelog: List<String> = emptyList(),
)
