package com.kakaanime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            KakaAnimeTheme(themeState = themeState) {
                KakaUiShell()
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun KakaUiShell() {
    var selectedTab by remember { mutableStateOf(KakaTab.HOME) }

    Scaffold(
        bottomBar = {
            KakaBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        },
    ) { padding ->
        androidx.compose.foundation.layout.Box(Modifier.padding(padding)) {
            if (selectedTab == KakaTab.HOME) {
                HomeV1Screen(
                    state = demoHomeState(),
                    onAnimeClick = {},
                    onContinueWatchingClick = {},
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
