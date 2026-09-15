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
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.WorkspacePremium
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
    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Diamond & Premium", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .10f)) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Diamond, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("${state.diamonds} Diamond", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("1 diamond = 1 video", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.PlayCircle, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Dapatkan Diamond", fontWeight = FontWeight.Bold)
                }
                Text("Tonton iklan untuk mendapat ${state.adDiamondReward} diamond.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = onWatchAd) { Text("Tonton Iklan") }
            }
        }
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.WorkspacePremium, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("KakaAnime Premium", fontWeight = FontWeight.Bold)
                }
                Text("1080p + auto skip intro/outro + pengalaman tanpa batas diamond.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = onPremiumClick) { Text("Lihat Premium") }
            }
        }
    }
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
                if (isDiamondGate) Icons.Outlined.Diamond else Icons.Outlined.CardMembership,
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
                    EpisodeAccessReason.NO_DIAMONDS -> "Butuh $diamondCost diamond untuk membuka episode ini."
                    EpisodeAccessReason.PREMIUM_QUALITY -> "Kualitas 1080p hanya tersedia untuk Premium."
                    EpisodeAccessReason.PREMIUM_FEATURE -> "Auto skip intro/outro hanya tersedia untuk Premium."
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (isDiamondGate) {
                Button(onClick = onWatchAd, modifier = Modifier.fillMaxWidth()) {
                    Text("Tonton Iklan • +2 Diamond")
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
