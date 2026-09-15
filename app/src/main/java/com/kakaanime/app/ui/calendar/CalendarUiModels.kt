package com.kakaanime.app.ui.calendar

import com.kakaanime.app.ui.home.HomeAnimeUi

/** UI-only calendar models. Schedule/network logic stays outside the UI repository. */
data class CalendarEpisodeUi(
    val anime: HomeAnimeUi,
    val episode: Int,
    val timeLabel: String = "",
    val isNew: Boolean = true,
    val statusLabel: String = "Aired",
    val countdownLabel: String = "",
)

data class CalendarDayUi(
    val key: String,
    val label: String,
    val dateLabel: String,
    val episodes: List<CalendarEpisodeUi> = emptyList(),
    val isToday: Boolean = false,
)

data class CalendarUiState(
    val days: List<CalendarDayUi> = emptyList(),
    val selectedDayKey: String = "",
    val updates: List<CalendarEpisodeUi> = emptyList(),
)
