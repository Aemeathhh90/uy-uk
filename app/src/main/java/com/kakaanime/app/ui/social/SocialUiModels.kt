package com.kakaanime.app.ui.social

/** UI-only social models. Networking, identity, and persistence stay outside the UI repository. */
data class SocialFriendUi(
    val id: String,
    val username: String,
    val status: String = "Online",
)

data class SocialMessageUi(
    val id: String,
    val username: String,
    val preview: String,
    val timeLabel: String = "Baru saja",
    val unreadCount: Int = 0,
)

data class SocialChatGroupUi(
    val id: String,
    val name: String,
    val memberCount: Int = 0,
    val lastMessage: String = "Belum ada pesan",
    val timeLabel: String = "",
    val unreadCount: Int = 0,
)

data class SocialGroupMemberUi(
    val id: String,
    val username: String,
    val isSelf: Boolean = false,
)

data class SocialChatMessageUi(
    val id: String,
    val senderName: String,
    val text: String,
    val timeLabel: String = "Baru saja",
    val isMine: Boolean = false,
)

data class SocialChatUiState(
    val chatId: String,
    val title: String,
    val subtitle: String,
    val isGroup: Boolean = false,
    val members: List<SocialGroupMemberUi> = emptyList(),
    val messages: List<SocialChatMessageUi> = emptyList(),
)

enum class WatchTogetherVisibility(val label: String) {
    PUBLIC("Public"),
    PRIVATE("Private"),
}

data class WatchTogetherParticipantUi(
    val id: String,
    val username: String,
    val status: String = "Ready",
    val isHost: Boolean = false,
)

data class WatchTogetherRoomUi(
    val id: String,
    val name: String,
    val animeTitle: String,
    val episode: Int,
    val visibility: WatchTogetherVisibility = WatchTogetherVisibility.PUBLIC,
    val hostName: String = "Akun Saya",
    val roomCode: String = "",
    val participants: List<WatchTogetherParticipantUi> = emptyList(),
    val isLive: Boolean = true,
)

data class WatchTogetherUiState(
    val isPremium: Boolean = false,
    val publicRooms: List<WatchTogetherRoomUi> = emptyList(),
)

data class SocialUiState(
    val username: String = "Akun Saya",
    val friends: List<SocialFriendUi> = emptyList(),
    val messages: List<SocialMessageUi> = emptyList(),
    val chatGroups: List<SocialChatGroupUi> = emptyList(),
    val globalOnlineCount: Int = 0,
    val animeRoomCount: Int = 0,
)
