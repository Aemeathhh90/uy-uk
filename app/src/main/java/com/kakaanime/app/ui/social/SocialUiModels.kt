package com.kakaanime.app.ui.social

/** UI-only social models. Networking, identity, and persistence stay outside the UI repository. */
data class SocialFriendUi(
    val id: String,
    val username: String,
    val status: String = "Online",
)

data class SocialUiState(
    val username: String = "Akun Saya",
    val friends: List<SocialFriendUi> = emptyList(),
    val globalOnlineCount: Int = 0,
    val animeRoomCount: Int = 0,
)
