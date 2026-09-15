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
import com.kakaanime.app.ui.social.SocialScreen
import com.kakaanime.app.ui.social.SocialUiState
import com.kakaanime.app.ui.social.SocialFriendUi
import com.kakaanime.app.ui.social.SocialMessageUi
import com.kakaanime.app.ui.theme.KakaAnimeTheme
import com.kakaanime.app.ui.theme.KakaThemeState
import com.kakaanime.app.ui.theme.ThemeCustomizationScreen
import com.kakaanime.app.ui.theme.rememberKakaThemeState

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
    data class Detail(val anime: HomeAnimeUi) : KakaDestination { override val rank = 7 }
    data object Monetization : KakaDestination { override val rank = 8 }
}

@Composable
private fun KakaUiShell(themeState: KakaThemeState) {
    var selectedTab by remember { mutableStateOf(KakaTab.HOME) }
    var selectedAnime by remember { mutableStateOf<HomeAnimeUi?>(null) }
    var favorites by remember { mutableStateOf(setOf("solo-leveling")) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }
    var selectedChat by remember { mutableStateOf<SocialChatUiState?>(null) }
    var showMonetization by remember { mutableStateOf(false) }
    var gateReason by remember { mutableStateOf<EpisodeAccessReason?>(null) }
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

    val hasNestedUi = gateReason != null || overlay != null || showMonetization || selectedAnime != null || selectedUserId != null || selectedChat != null || selectedTab != KakaTab.HOME
    BackHandler(enabled = hasNestedUi) {
        when {
            gateReason != null -> gateReason = null
            overlay != null -> overlay = null
            showMonetization -> showMonetization = false
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
        bottomBar = { if (selectedAnime == null && selectedUserId == null && selectedChat == null && !showMonetization && overlay == null) KakaBottomNavigation(selectedTab = selectedTab, onTabSelected = { profileFromAvatar = false; selectedTab = it }) },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (overlay == null) {
                KakaAnimatedScreen(
                    targetState = destination,
                    rank = { it.rank },
                    deepNavigation = { from, to ->
                        from is KakaDestination.Detail || to is KakaDestination.Detail ||
                            from is KakaDestination.Chat || to is KakaDestination.Chat ||
                            from is KakaDestination.OtherProfile || to is KakaDestination.OtherProfile ||
                            from is KakaDestination.Monetization || to is KakaDestination.Monetization ||
                            (profileFromAvatar && (to is KakaDestination.Profile || from is KakaDestination.Profile))
                    },
                ) { target ->
                    when (target) {
                        is KakaDestination.Chat -> SocialChatScreen(
                            state = target.state,
                            onBack = { selectedChat = null },
                            onSendMessage = {},
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
                                onWatchTogetherClick = {},
                                onMessageClick = { selectedChat = demoDirectChat(it) },
                                onGroupClick = { selectedChat = demoGroupChat(it) },
                            )
                        }
                        KakaDestination.Library -> LibraryScreen(
                            state = LibraryUiState(favorites = demoHomeState().anime.filter { it.id in favorites }),
                            onAnimeClick = { selectedAnime = it }, onFavoriteToggle = { anime -> favorites = favorites - anime.id },
                        )
                        KakaDestination.Profile -> MyProfileScreen(
                            state = demoMyProfile(favorites, profile),
                            onEditProfile = { overlay = OverlayScreen.EDIT_PROFILE },
                            onAppearanceClick = { overlay = OverlayScreen.THEME },
                            onAnimeClick = { id -> demoHomeState().anime.firstOrNull { it.id == id }?.let { selectedAnime = it } },
                        )
                        KakaDestination.Calendar -> CalendarScreen(state = demoCalendarState(), onAnimeClick = { selectedAnime = it })
                    }
                }
            } else {
                AnimatedVisibility(
                    visible = true,
                    enter = KakaMotion.modalEnterTransition,
                    exit = KakaMotion.modalExitTransition,
                ) {
                    when (overlay) {
                        OverlayScreen.NOTIFICATIONS -> NotificationCenterScreen(
                            state = notifications,
                            onBack = { overlay = null },
                            onNotificationClick = { notification ->
                                notification.animeId?.let { id -> demoHomeState().anime.firstOrNull { it.id == id }?.let { selectedAnime = it } }
                                notifications = NotificationUiState(notifications.notifications.map { if (it.id == notification.id) it.copy(isUnread = false) else it })
                                overlay = null
                            },
                            onMarkAllRead = { notifications = NotificationUiState(notifications.notifications.map { it.copy(isUnread = false) }) },
                        )
                        OverlayScreen.EDIT_PROFILE -> EditProfileScreen(
                            state = profile,
                            onBack = { overlay = null },
                            onSave = { profile = it; overlay = null },
                        )
                        OverlayScreen.THEME -> ThemeCustomizationScreen(
                            state = themeState,
                            onBack = { overlay = null },
                        )
                        null -> Unit
                    }
                }
            }
            gateReason?.let { reason ->
                Dialog(onDismissRequest = { gateReason = null }) {
                    AnimatedVisibility(
                        visible = true,
                        enter = KakaMotion.modalEnterTransition,
                        exit = KakaMotion.modalExitTransition,
                    ) {
                        EpisodeGateDialog(
                            reason = reason,
                            onWatchAd = { gateReason = null },
                            onPremiumClick = { gateReason = null; showMonetization = true },
                            onDismiss = { gateReason = null },
                        )
                    }
                }
            }
        }
    }
}

private fun demoSocialState(username: String) = SocialUiState(
    username = username,
    globalOnlineCount = 128,
    animeRoomCount = 12,
    friends = listOf(
        SocialFriendUi("rin", "Rin", "Online"),
        SocialFriendUi("yuki", "Yuki", "Nonton One Piece"),
        SocialFriendUi("akira", "Akira", "Online"),
    ),
    messages = listOf(
        SocialMessageUi("m-rin", "Rin", "Episode barunya gila sih", "Baru saja", 2),
        SocialMessageUi("m-yuki", "Yuki", "Nanti nonton bareng?", "5 mnt", 0),
        SocialMessageUi("m-akira", "Akira", "Aku baru selesai Solo Leveling", "12 mnt", 0),
    ),
    chatGroups = listOf(
        SocialChatGroupUi("g-op", "One Piece Indonesia", 184, "Chapter barunya rame", "3 mnt", 4),
        SocialChatGroupUi("g-sl", "Solo Leveling", 96, "Sung Jinwoo lagi dibahas", "18 mnt", 0),
        SocialChatGroupUi("g-jjk", "Jujutsu Kaisen", 121, "Ada teori baru", "32 mnt", 1),
    ),
)

private fun demoDirectChat(message: SocialMessageUi) = SocialChatUiState(
    chatId = message.id,
    title = message.username,
    subtitle = "Online",
    messages = listOf(
        SocialChatMessageUi("1", message.username, message.preview, "Baru saja"),
        SocialChatMessageUi("2", "Akun Saya", "Iya, seru banget. 😭", "Baru saja", true),
        SocialChatMessageUi("3", message.username, "Wajib lanjut sampai episode terbaru.", "1 mnt lalu"),
    ),
)

private fun demoGroupChat(group: SocialChatGroupUi) = SocialChatUiState(
    chatId = group.id,
    title = group.name,
    subtitle = "${group.memberCount} anggota • Grup anime",
    isGroup = true,
    messages = listOf(
        SocialChatMessageUi("1", "Rin", group.lastMessage, "${group.timeLabel} lalu"),
        SocialChatMessageUi("2", "Yuki", "Menurut kalian episode terbaru gimana?", "2 mnt lalu"),
        SocialChatMessageUi("3", "Akun Saya", "Bagian akhirnya bikin penasaran banget.", "Baru saja", true),
    ),
)

private fun demoMyProfile(favorites: Set<String>, profile: EditProfileUiState) = ProfileUiState(userId = "self", username = profile.username, bio = profile.bio, status = profile.status, favorites = demoHomeState().anime.filter { it.id in favorites }, watchingTitles = listOf("One Piece"), watchedCount = 42, favoriteCount = favorites.size, followingCount = 8, isSelf = true)
private fun demoOtherProfile(userId: String) = ProfileUiState(userId = userId, username = when (userId) { "rin" -> "Rin"; "yuki" -> "Yuki"; else -> "Akira" }, bio = "Suka anime action dan fantasy.", status = "Online", favorites = demoHomeState().anime.take(2), watchingTitles = listOf("Solo Leveling"), watchedCount = 86, favoriteCount = 2, followingCount = 21, isSelf = false, isFollowing = false)
private fun demoDetail(anime: HomeAnimeUi) = AnimeDetailUi(id = anime.id, title = anime.title, description = "Cerita ${anime.title} dengan petualangan, konflik, dan karakter yang terus berkembang.", genre = anime.genre, year = "2026", type = "TV", status = anime.status, studio = "Kaka Studio", season = "Season 1", rating = anime.rating, posterUrl = anime.posterUrl)
private fun demoEpisodes() = (1..12).map { number -> EpisodeUi(number, "Episode $number", isNew = number >= 11, isWatched = number <= 3, isLocked = number > 3) }
private fun demoCalendarState(): CalendarUiState {
    val anime = demoHomeState().anime
    return CalendarUiState(days = listOf(CalendarDayUi("mon", "Senin", "15 Sep", listOf(CalendarEpisodeUi(anime[0], 1150, "18:00"))), CalendarDayUi("tue", "Selasa", "16 Sep", listOf(CalendarEpisodeUi(anime[1], 25, "20:00"))), CalendarDayUi("wed", "Rabu", "17 Sep", listOf(CalendarEpisodeUi(anime[2], 49, "19:30"))), CalendarDayUi("thu", "Kamis", "18 Sep", emptyList()), CalendarDayUi("fri", "Jumat", "19 Sep", listOf(CalendarEpisodeUi(anime[3], 64, "21:00"))), CalendarDayUi("sat", "Sabtu", "20 Sep", emptyList()), CalendarDayUi("sun", "Minggu", "21 Sep", emptyList())), selectedDayKey = "mon", updates = listOf(CalendarEpisodeUi(anime[0], 1150, "Baru rilis", true), CalendarEpisodeUi(anime[1], 25, "Baru rilis", true), CalendarEpisodeUi(anime[2], 49, "Baru rilis", true)))
}
private fun demoHomeState() = HomeUiState(
    anime = listOf(HomeAnimeUi("one-piece", "One Piece", 1150, "8.9", "Action • Adventure", isNew = true), HomeAnimeUi("solo-leveling", "Solo Leveling", 25, "8.8", "Action • Fantasy", isNew = true), HomeAnimeUi("jujutsu-kaisen", "Jujutsu Kaisen", 48, "8.7", "Action • Supernatural"), HomeAnimeUi("demon-slayer", "Demon Slayer", 63, "8.6", "Action • Fantasy")),
    continueWatching = listOf(ContinueWatchingUi(HomeAnimeUi("one-piece", "One Piece", 1150, "8.9", "Action • Adventure"), 1149, "Episode 1149", progressPercent = 42)),
    diamonds = 6, isPremium = false, username = "Akun Saya",
)
