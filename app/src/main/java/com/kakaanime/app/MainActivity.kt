package com.kakaanime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kakaanime.app.ui.detail.AnimeDetailScreen
import com.kakaanime.app.ui.detail.AnimeDetailUi
import com.kakaanime.app.ui.detail.EpisodeUi
import com.kakaanime.app.ui.detail.SeasonUi
import com.kakaanime.app.ui.home.ContinueWatchingUi
import com.kakaanime.app.ui.home.HomeAnimeUi
import com.kakaanime.app.ui.home.HomeUiState
import com.kakaanime.app.ui.home.HomeV1Screen
import com.kakaanime.app.ui.theme.KakaAnimeTheme
import com.kakaanime.app.ui.theme.rememberKakaThemeState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState = rememberKakaThemeState()
            KakaAnimeTheme(themeState = themeState) { KakaUiShell() }
        }
    }
}

@androidx.compose.runtime.Composable
private fun KakaUiShell() {
    var selectedTab by remember { mutableStateOf(KakaTab.HOME) }
    var selectedAnime by remember { mutableStateOf<HomeAnimeUi?>(null) }

    Scaffold(
        bottomBar = {
            if (selectedAnime == null) {
                KakaBottomNavigation(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (selectedAnime != null) {
                val anime = selectedAnime!!
                AnimeDetailScreen(
                    anime = demoDetail(anime),
                    episodes = demoEpisodes(),
                    seasons = listOf(SeasonUi("s1", "Season 1"), SeasonUi("s2", "Season 2")),
                    isFavorite = false,
                    onBack = { selectedAnime = null },
                    onToggleFavorite = {},
                    onEpisodeClick = {},
                    onSeasonSelected = {},
                    onEpisodeGate = {},
                )
            } else if (selectedTab == KakaTab.HOME) {
                HomeV1Screen(
                    state = demoHomeState(),
                    onAnimeClick = { selectedAnime = it },
                    onContinueWatchingClick = { selectedAnime = it.anime },
                    onProfileClick = {},
                    onNotificationsClick = {},
                    onDiamondClick = {},
                    onPremiumClick = {},
                    onWatchTogetherClick = {},
                )
            }
        }
    }
}

private fun demoDetail(anime: HomeAnimeUi) = AnimeDetailUi(
    id = anime.id,
    title = anime.title,
    description = "Cerita ${anime.title} dengan petualangan, konflik, dan karakter yang terus berkembang.",
    genre = anime.genre,
    year = "2026",
    type = "TV",
    status = anime.status,
    studio = "Kaka Studio",
    season = "Season 1",
    rating = anime.rating,
    posterUrl = anime.posterUrl,
)

private fun demoEpisodes() = (1..12).map { number ->
    EpisodeUi(
        number = number,
        title = "Episode $number",
        isNew = number >= 11,
        isWatched = number <= 3,
        isLocked = number > 3,
    )
}

private fun demoHomeState() = HomeUiState(
    anime = listOf(
        HomeAnimeUi("one-piece", "One Piece", 1150, "8.9", "Action • Adventure", isNew = true),
        HomeAnimeUi("solo-leveling", "Solo Leveling", 25, "8.8", "Action • Fantasy", isNew = true),
        HomeAnimeUi("jujutsu-kaisen", "Jujutsu Kaisen", 48, "8.7", "Action • Supernatural"),
        HomeAnimeUi("demon-slayer", "Demon Slayer", 63, "8.6", "Action • Fantasy"),
    ),
    continueWatching = listOf(
        ContinueWatchingUi(
            anime = HomeAnimeUi("one-piece", "One Piece", 1150, "8.9", "Action • Adventure"),
            episode = 1149,
            episodeTitle = "Episode 1149",
            progressPercent = 42,
        ),
    ),
    diamonds = 6,
    isPremium = false,
    username = "Akun Saya",
)
