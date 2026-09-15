package com.kakaanime.app.ui.player

/** UI-only playback state. Media3/player/source logic stays outside the UI repository. */
data class PlayerUiState(
    val animeTitle: String,
    val episode: Int,
    val episodeTitle: String = "",
    val progressPercent: Int = 0,
    val isPlaying: Boolean = false,
    val isPremium: Boolean = false,
    val quality: PlayerQuality = PlayerQuality.P720,
    val canSkipIntro: Boolean = false,
    val showSkipIntro: Boolean = false,
    val canSkipOutro: Boolean = false,
    val showSkipOutro: Boolean = false,
    val hasPreviousEpisode: Boolean = false,
    val hasNextEpisode: Boolean = true,
    val playbackState: PlayerPlaybackState = PlayerPlaybackState.READY,
    val errorMessage: String? = null,
)

enum class PlayerPlaybackState {
    LOADING,
    READY,
    BUFFERING,
    ERROR,
    COMPLETED,
}

enum class PlayerQuality(val label: String) {
    P720("720p"),
    P1080("1080p"),
}
