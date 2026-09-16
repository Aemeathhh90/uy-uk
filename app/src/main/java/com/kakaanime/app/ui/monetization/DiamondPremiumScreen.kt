package com.kakaanime.app.ui.monetization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardMembership
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DiamondPremiumScreen(
    state: MonetizationUiState,
    onWatchAd: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
) {
    PremiumScreen(
        state = state,
        onWatchAd = onWatchAd,
        onSubscribe = { onPremiumClick() },
    )
}

/**
 * Compact episode access gate.
 * Keep this dialog focused on one decision: unlock this episode or dismiss.
 * Reward/billing execution stays outside the UI repository.
 */
@Composable
fun EpisodeGateDialog(
    reason: EpisodeAccessReason,
    diamondCost: Int = 1,
    onWatchAd: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val isDiamondGate = reason == EpisodeAccessReason.NO_DIAMONDS

    Surface(
        shape = RoundedCornerShape(22.dp),
        tonalElevation = 6.dp,
        modifier = Modifier.padding(24.dp),
    ) {
        Column(
            Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                if (isDiamondGate) Icons.Outlined.Key else Icons.Outlined.CardMembership,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                if (isDiamondGate) "Episode Terkunci" else "Fitur Premium",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )

            Text(
                when (reason) {
                    EpisodeAccessReason.NO_DIAMONDS -> "Butuh $diamondCost Key untuk membuka episode ini."
                    EpisodeAccessReason.PREMIUM_QUALITY -> "Kualitas 1080p hanya tersedia untuk Premium."
                    EpisodeAccessReason.PREMIUM_FEATURE -> "Auto skip intro/outro hanya tersedia untuk Premium."
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (isDiamondGate) {
                Button(onClick = onWatchAd, modifier = Modifier.fillMaxWidth()) {
                    Text("Tonton Iklan • +2 Key")
                }
            } else {
                Button(onClick = onPremiumClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Upgrade Premium")
                }
            }

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Nanti")
            }
        }
    }
}
