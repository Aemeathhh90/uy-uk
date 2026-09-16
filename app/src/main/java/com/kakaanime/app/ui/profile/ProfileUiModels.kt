package com.kakaanime.app.ui.profile

import com.kakaanime.app.ui.home.HomeAnimeUi

data class ProfileBadgeUi(
    val id: String,
    val name: String,
    val description: String = "",
    val isUnlocked: Boolean = false,
    val isEquipped: Boolean = false,
)

data class ProfileUiState(
    val userId: String,
    val username: String,
    val bio: String = "",
    val status: String = "Online",
    val favorites: List<HomeAnimeUi> = emptyList(),
    val watchingTitles: List<String> = emptyList(),
    val watchedCount: Int = 0,
    val favoriteCount: Int = 0,
    val followingCount: Int = 0,
    val isSelf: Boolean = false,
    val isFollowing: Boolean = false,
    val supportPoints: Int = 0,
    val supporterLevel: String = "Supporter",
    val supporterLevelStartPoints: Int = 0,
    val nextSupporterLevelPoints: Int = 25,
    val nextSupporterReward: String = "Supporter Border",
    val supporterTitle: String = "Supporter",
    val supporterBadge: String = "Supporter",
    val supporterBorder: String = "Basic",
    val badges: List<ProfileBadgeUi> = emptyList(),
    val equippedBadgeIds: Set<String> = emptySet(),
    val maxEquippedBadges: Int = 5,
)