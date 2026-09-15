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

data class SocialUiState(
    val username: String = "Akun Saya",
    val friends: List<SocialFriendUi> = emptyList(),
    val messages: List<SocialMessageUi> = emptyList(),
    val chatGroups: List<SocialChatGroupUi> = emptyList(),
    val globalOnlineCount: Int = 0,
    val animeRoomCount: Int = 0,
)
