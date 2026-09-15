package com.kakaanime.app.ui.notifications

data class NotificationUi(
    val id: String,
    val title: String,
    val message: String,
    val timeLabel: String = "Baru saja",
    val animeId: String? = null,
    val episode: Int? = null,
    val isUnread: Boolean = true,
)

data class NotificationUiState(
    val notifications: List<NotificationUi> = emptyList(),
) {
    val unreadCount: Int
        get() = notifications.count { it.isUnread }
}
