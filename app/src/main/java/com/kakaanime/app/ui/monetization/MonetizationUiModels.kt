package com.kakaanime.app.ui.monetization

/** UI-only monetization state. Billing, ads, and entitlement logic stay outside the UI repository. */
data class MonetizationUiState(
    val diamonds: Int = 0,
    val isPremium: Boolean = false,
    val videoDiamondCost: Int = 1,
    val adDiamondReward: Int = 2,
    val qualityLabel: String = "720p",
)

enum class EpisodeAccessReason { NO_DIAMONDS, PREMIUM_QUALITY, PREMIUM_FEATURE }
