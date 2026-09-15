package com.kakaanime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kakaanime.app.ui.detail.AnimeDetailScreen
import com.kakaanime.app.ui.detail.AnimeDetailUi
import com.kakaanime.app.ui.detail.EpisodeUi
import com.kakaanime.app.ui.detail.SeasonUi
import com.kakaanime.app.ui.home.ContinueWatchingUi
import com.kakaanime.app.ui.home.HomeAnimeUi
import com.kakaanime.app.ui.home.HomeUiState
import com.kakaanime.app.ui.home.HomeV1Screen
import com.kakaanime.app.ui.library.LibraryScreen
import com.kakaanime.app.ui.library.LibraryUiState
import com.kakaanime.app.ui.profile.MyProfileScreen
import com.kakaanime.app.ui.profile.OtherUserProfileScreen
import com.kakaanime.app.ui.profile.ProfileUiState
import com.kakaanime.app.ui.social.SocialScreen
import com.kakaanime.app.ui.social.SocialUiState
import com.kakaanime.app.ui.social.SocialFriendUi
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

@Composable
private fun KakaUiShell() {
    var selectedTab by remember { mutableStateOf(KakaTab.HOME) }
    var selectedAnime by remember { mutableStateOf<HomeAnimeUi?>(null) }
    var favorites by remember { mutableStateOf(setOf("solo-leveling")) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            if (selectedAnime == null && selectedUserId == null) {
                KakaBottomNavigation(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when {
                selectedAnime != null -> {
                    val anime = selectedAnime!!
                    AnimeDetailScreen(
                        anime = demoDetail(anime),
                        episodes = demoEpisodes(),
                        seasons = listOf(SeasonUi("s1", "Season 1"), SeasonUi("s2", "Season 2")),
                        isFavorite = anime.id in favorites,
                        onBack = { selectedAnime = null },
                        onToggleFavorite = {
                            favorites = if (anime.id in favorites) favorites - anime.id else favorites + anime.id
                        },
                        onEpisodeClick = {},
                        onSeasonSelected = {},
                        onEpisodeGate = {},
                    )
                }
                selectedUserId != null -> {
                    OtherUserProfileScreen(
                        state = demoOtherProfile(selectedUserId!!),
                        onBack = { selectedUserId = null },
                        onFollowToggle = {},
                        onAnimeClick = { value ->
                            demoHomeState().anime.firstOrNull { it.id == value || it.title == value }?.let { selectedAnime = it }
                        },
                    )
                }
                else -> {
                    when (selectedTab) {
                        KakaTab.HOME -> HomeV1Screen(
                            state = demoHomeState(),
                            onAnimeClick = { selectedAnime = it },
                            onContinueWatchingClick = { selectedAnime = it.anime },
                            onProfileClick = { selectedUserId = "self" },
                            onNotificationsClick = {},
                            onDiamondClick = {},
                            onPremiumClick = {},
                            onWatchTogetherClick = {},
                        )
                        KakaTab.SOCIAL -> SocialScreen(
                            state = demoSocialState(),
                            onProfileClick = { selectedUserId = it },
                            onWatchTogetherClick = {},
                        )
                        KakaTab.LIBRARY -> LibraryScreen(
                            state = LibraryUiState(favorites = demoHomeState().anime.filter { it.id in favorites }),
                            onAnimeClick = { selectedAnime = it },
                            onFavoriteToggle = { anime -> favorites = favorites - anime.id },
                        )
                        KakaTab.PROFILE -> MyProfileScreen(
                            state = demoMyProfile(favorites),
                            onEditProfile = {},
                            onAnimeClick = { id ->
                                demoHomeState().anime.firstOrNull { it.id == id }?.let { selectedAnime = it }
                            },
                        )
                        KakaTab.CALENDAR -> Box(Modifier) {}
                    }
                }
            }
        }
    }
}

private fun demoSocialState() = SocialUiState(
    username = "Akun Saya",
    globalOnlineCount = 128,
    animeRoomCount = 12,
    friends = listOf(
        SocialFriendUi("rin", "Rin", "Online"),
        SocialFriendUi("yuki", "Yuki", "Nonton One Piece"),
        SocialFriendUi("akira", "Akira", "Online"),
    ),
)

private fun demoMyProfile(favorites: Set<String>) = ProfileUiState(
    userId = "self",
    username = "Akun Saya",
    bio = "Pecinta anime dan nonton bareng.",
    status = "Online",
    favorites = demoHomeState().anime.filter { it.id in favorites },
    watchingTitles = listOf("One Piece"),
    watchedCount = 42,
    favoriteCount = favorites.size,
    followingCount = 8,
    isSelf = true,
)

private fun demoOtherProfile(userId: String) = ProfileUiState(
    userId = userId,
    username = when (userId) { "rin" -> "Rin"; "yuki" -> "Yuki"; else -> "Akira" },
    bio = "Suka anime action dan fantasy.",
    status = "Online",
    favorites = demoHomeState().anime.take(2),
    watchingTitles = listOf("Solo Leveling"),
    watchedCount = 86,
    favoriteCount = 2,
    followingCount = 21,
    isSelf = false,
    isFollowing = false,
)

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
