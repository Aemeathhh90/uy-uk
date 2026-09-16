package com.kakaanime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.kakaanime.app.ui.calendar.CalendarDayUi
import com.kakaanime.app.ui.calendar.CalendarEpisodeUi
import com.kakaanime.app.ui.calendar.CalendarScreen
import com.kakaanime.app.ui.calendar.CalendarUiState
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
import com.kakaanime.app.ui.monetization.DiamondPremiumScreen
import com.kakaanime.app.ui.monetization.EpisodeAccessReason
import com.kakaanime.app.ui.monetization.EpisodeGateDialog
import com.kakaanime.app.ui.monetization.MonetizationUiState
import com.kakaanime.app.ui.motion.KakaAnimatedScreen
import com.kakaanime.app.ui.motion.KakaMotion
import com.kakaanime.app.ui.notifications.NotificationCenterScreen
import com.kakaanime.app.ui.notifications.NotificationUi
import com.kakaanime.app.ui.notifications.NotificationUiState
import com.kakaanime.app.ui.profile.EditProfileScreen
import com.kakaanime.app.ui.profile.EditProfileUiState
import com.kakaanime.app.ui.profile.MyProfileScreen
import com.kakaanime.app.ui.profile.OtherUserProfileScreen
import com.kakaanime.app.ui.profile.ProfileUiState
import com.kakaanime.app.ui.social.SocialChatGroupUi
import com.kakaanime.app.ui.social.SocialChatMessageUi
import com.kakaanime.app.ui.social.SocialChatScreen
import com.kakaanime.app.ui.social.SocialChatUiState
import com.kakaanime.app.ui.social.SocialGroupInfoScreen
import com.kakaanime.app.ui.social.SocialScreen
import com.kakaanime.app.ui.social.SocialUiState
import com.kakaanime.app.ui.social.SocialFriendUi
import com.kakaanime.app.ui.social.SocialMessageUi
import com.kakaanime.app.ui.social.SocialGroupMemberUi
import com.kakaanime.app.ui.theme.KakaAnimeTheme
import com.kakaanime.app.ui.theme.KakaThemeState
import com.kakaanime.app.ui.theme.ThemeCustomizationScreen
import com.kakaanime.app.ui.theme.rememberKakaThemeState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState = rememberKakaThemeState()
            KakaAnimeTheme(themeState = themeState) { KakaUiShell(themeState) }
        }
    }
}

private enum class OverlayScreen { NOTIFICATIONS, EDIT_PROFILE, THEME }

private sealed interface KakaDestination {
    val rank: Int
    data object Home : KakaDestination { override val rank = 0 }
    data object Calendar : KakaDestination { override val rank = 1 }
    data object Social : KakaDestination { override val rank = 2 }
    data object Library : KakaDestination { override val rank = 3 }
    data object Profile : KakaDestination { override val rank = 4 }
    data class OtherProfile(val userId: String) : KakaDestination { override val rank = 5 }
    data class Chat(val state: SocialChatUiState) : KakaDestination { override val rank = 6 }
    data class GroupInfo(val state: SocialChatUiState) : KakaDestination { override val rank = 7 }
    data class Detail(val anime: HomeAnimeUi) : KakaDestination { override val rank = 8 }
    data object Monetization : KakaDestination { override val rank = 9 }
}

@Composable
private fun KakaUiShell(themeState: KakaThemeState) {
    var selectedTab by remember { mutableStateOf(KakaTab.HOME) }
    var selectedAnime by remember { mutableStateOf<HomeAnimeUi?>(null) }
    var favorites by remember { mutableStateOf(setOf("solo-leveling")) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }
    var selectedChat by remember { mutableStateOf<SocialChatUiState?>(null) }
    var selectedGroupInfo by remember { mutableStateOf<SocialChatUiState?>(null) }
    var showMonetization by remember { mutableStateOf(false) }
    var gateReason by remember { mutableStateOf<EpisodeAccessReason?>(null) }
    var gateVisible by remember { mutableStateOf(false) }
    var overlay by remember { mutableStateOf<OverlayScreen?>(null) }
    var profileFromAvatar by remember { mutableStateOf(false) }
    var profile by remember { mutableStateOf(EditProfileUiState("Akun Saya", "Pecinta anime dan nonton bareng.", "Online")) }
    var notifications by remember {
        mutableStateOf(NotificationUiState(listOf(
            NotificationUi("n1", "Episode baru tersedia", "One Piece Episode 1150 sudah rilis.", "Baru saja", "one-piece", 1150, true),
            NotificationUi("n2", "Update favorit", "Solo Leveling mendapatkan episode baru.", "1 jam lalu", "solo-leveling", 25, true),
        )))
    }
    val monetizationState = MonetizationUiState(diamonds = 6, isPremium = false)

    LaunchedEffect(gateReason) {
        if (gateReason != null) {
            gateVisible = true
        } else if (gateVisible) {
            delay(150)
            gateVisible = false
        }
    }

    val hasNestedUi = gateReason != null || gateVisible || overlay != null || showMonetization || selectedAnime != null || selectedUserId != null || selectedChat != null || selectedGroupInfo != null || selectedTab != KakaTab.HOME
    BackHandler(enabled = hasNestedUi) {
        when {
            gateReason != null -> gateReason = null
            overlay != null -> overlay = null
            showMonetization -> showMonetization = false
            selectedGroupInfo != null -> selectedGroupInfo = null
            selectedChat != null -> selectedChat = null
            selectedAnime != null -> selectedAnime = null
            selectedUserId != null -> selectedUserId = null
            selectedTab != KakaTab.HOME -> {
                selectedTab = KakaTab.HOME
                profileFromAvatar = false
            }
        }
    }

    val destination: KakaDestination = when {
        selectedGroupInfo != null -> KakaDestination.GroupInfo(selectedGroupInfo!!)
        selectedChat != null -> KakaDestination.Chat(selectedChat!!)
        selectedAnime != null -> KakaDestination.Detail(selectedAnime!!)
        selectedUserId != null -> KakaDestination.OtherProfile(selectedUserId!!)
        showMonetization -> KakaDestination.Monetization
        else -> when (selectedTab) {
            KakaTab.HOME -> KakaDestination.Home
            KakaTab.CALENDAR -> KakaDestination.Calendar
            KakaTab.SOCIAL -> KakaDestination.Social
            KakaTab.LIBRARY -> KakaDestination.Library
            KakaTab.PROFILE -> KakaDestination.Profile
        }
    }

    Scaffold(
        bottomBar = { if (selectedAnime == null && selectedUserId == null && selectedChat == null && selectedGroupInfo == null && !showMonetization && overlay == null) KakaBottomNavigation(selectedTab = selectedTab, onTabSelected = { profileFromAvatar = false; selectedTab = it }) },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (overlay == null) {
                KakaAnimatedScreen(
                    targetState = destination,
                    rank = { it.rank },
                    deepNavigation = { from, to ->
                        from is KakaDestination.Detail || to is KakaDestination.Detail ||
                            from is KakaDestination.Chat || to is KakaDestination.Chat ||
                            from is KakaDestination.GroupInfo || to is KakaDestination.GroupInfo ||
                            from is KakaDestination.OtherProfile || to is KakaDestination.OtherProfile ||
                            from is KakaDestination.Monetization || to is KakaDestination.Monetization ||
                            (profileFromAvatar && (to is KakaDestination.Profile || from is KakaDestination.Profile))
                    },
                ) { target ->
                    when (target) {
                        is KakaDestination.GroupInfo -> SocialGroupInfoScreen(
                            state = target.state,
                            availableFriends = demoSocialState(profile.username).friends,
                            onBack = { selectedGroupInfo = null },
                            onMembersChanged = { members ->
                                selectedGroupInfo = selectedGroupInfo?.copy(members = members, subtitle = "${members.size} anggota • Grup anime")
                                selectedChat = selectedChat?.copy(members = members, subtitle = "${members.size} anggota • Grup anime")
                            },
                        )
                        is KakaDestination.Chat -> SocialChatScreen(
                            state = target.state,
                            onBack = { selectedChat = null },
                            onSendMessage = {},
                            onGroupInfoClick = if (target.state.isGroup) {
                                { selectedGroupInfo = selectedChat }
                            } else {
                                {}
                            },
                        )
                        is KakaDestination.Detail -> {
                            val anime = target.anime
                            AnimeDetailScreen(
                                anime = demoDetail(anime), episodes = demoEpisodes(),
                                seasons = listOf(SeasonUi("s1", "Season 1"), SeasonUi("s2", "Season 2")),
                                isFavorite = anime.id in favorites,
                                onBack = { selectedAnime = null },
                                onToggleFavorite = { favorites = if (anime.id in favorites) favorites - anime.id else favorites + anime.id },
                                onEpisodeClick = {}, onSeasonSelected = {},
                                onEpisodeGate = { gateReason = EpisodeAccessReason.NO_DIAMONDS },
                            )
                        }
                        is KakaDestination.OtherProfile -> OtherUserProfileScreen(
                            state = demoOtherProfile(target.userId),
                            onBack = { selectedUserId = null }, onFollowToggle = {},
                            onAnimeClick = { value -> demoHomeState().anime.firstOrNull { it.id == value || it.title == value }?.let { selectedAnime = it } },
                        )
                        KakaDestination.Monetization -> DiamondPremiumScreen(state = monetizationState, onWatchAd = {}, onPremiumClick = {})
                        KakaDestination.Home -> HomeV1Screen(
                            state = demoHomeState().copy(username = profile.username),
                            onAnimeClick = { selectedAnime = it }, onContinueWatchingClick = { selectedAnime = it.anime },
                            onProfileClick = { profileFromAvatar = true; selectedTab = KakaTab.PROFILE },
                            onNotificationsClick = { overlay = OverlayScreen.NOTIFICATIONS },
                            onDiamondClick = { showMonetization = true }, onPremiumClick = { showMonetization = true }, onWatchTogetherClick = {},
                        )
                        KakaDestination.Social -> {
                            val socialState = demoSocialState(profile.username)
                            SocialScreen(
                                state = socialState,
                                onProfileClick = { selectedUserId = it },
                                onWatchTogetherClick = { showMonetization = true },
                                onMessageClick = { selectedChat = demoDirectChat(it) },
                                onGroupClick = { selectedChat = demoGroupChat(it) },
                            )
                        }
                        KakaDestination.Library -> LibraryScreen(
                            state = LibraryUiState(favorites = demoHomeState().anime.filter { it.id in favorites }),
