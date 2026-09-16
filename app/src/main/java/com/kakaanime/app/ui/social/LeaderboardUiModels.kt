package com.kakaanime.app.ui.social

enum class LeaderboardWatcherMode(val label: String) {
    SOLO("Solo"),
    WATCH_TOGETHER("Watch Together"),
}

enum class LeaderboardPeriod(val label: String) {
    WEEKLY("Mingguan"),
    MONTHLY("Bulanan"),
    ALL_TIME("All Time"),
}

enum class LeaderboardCategory(val label: String) {
    WATCHER("Watcher"),
    SUPPORTER("Supporter"),
    ACHIEVEMENT("Achievement"),
}

data class LeaderboardEntryUi(
    val rank: Int,
    val username: String,
    val valueLabel: String,
    val subtitle: String = "",
    val isSelf: Boolean = false,
)

data class LeaderboardStatsUi(
    val soloWatchTime: String,
    val watchTogetherTime: String,
    val supportPoints: Int,
    val achievements: Int,
)

data class LeaderboardUiState(
    val username: String = "Akun Saya",
    val category: LeaderboardCategory = LeaderboardCategory.WATCHER,
    val watcherMode: LeaderboardWatcherMode = LeaderboardWatcherMode.SOLO,
    val period: LeaderboardPeriod = LeaderboardPeriod.WEEKLY,
    val entries: List<LeaderboardEntryUi> = emptyList(),
    val myStats: LeaderboardStatsUi = LeaderboardStatsUi("0m", "0m", 0, 0),
    val myRank: Int = 0,
    val myValueLabel: String = "0m",
    val previousRank: Int? = null,
)
