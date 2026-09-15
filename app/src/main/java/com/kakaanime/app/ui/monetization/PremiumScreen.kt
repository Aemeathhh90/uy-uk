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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class PremiumPlan(val label: String, val period: String) {
    MONTHLY("Bulanan", "/ bulan"),
    YEARLY("Tahunan", "/ tahun"),
}

@Composable
fun PremiumScreen(
    state: MonetizationUiState,
    onBack: () -> Unit = {},
    onSubscribe: (String) -> Unit = {},
    onWatchAd: () -> Unit = {},
) {
    var selectedPlan by remember { mutableStateOf(PremiumPlan.YEARLY) }

    Column(
        Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack) { Text("Kembali") }
            Spacer(Modifier.width(10.dp))
            Text("KakaAnime Premium", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        }

        Surface(
            Modifier.fillMaxWidth(),
            RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = .10f),
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.WorkspacePremium, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Text("Premium", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    if (state.isPremium) "Premium aktif di akun ini."
                    else "Buka kualitas dan fitur Premium tanpa mengubah alur nonton gratis.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Diamond, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("${state.diamonds} Diamond", fontWeight = FontWeight.Bold)
                    Text("${state.videoDiamondCost} diamond = 1 video untuk pengguna gratis", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (!state.isPremium) {
                    OutlinedButton(onClick = onWatchAd) { Text("+${state.adDiamondReward}") }
                }
            }
        }

        Text("Yang kamu dapat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        PremiumBenefit("1080p", "Kualitas hingga 1080p", enabled = true)
        PremiumBenefit("Auto Skip", "Lewati intro/outro secara otomatis", enabled = true)
        PremiumBenefit("Tanpa Diamond", "Nonton tanpa biaya diamond per episode", enabled = true)
        PremiumBenefit("Appearance", "Akses penuh kustomisasi tampilan Premium", enabled = true)
        PremiumBenefit("Watch Together", "Buat room Watch Together", enabled = true)

        if (!state.isPremium) {
            Text("Pilih paket", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PremiumPlanCard(
                    plan = PremiumPlan.MONTHLY,
                    selected = selectedPlan == PremiumPlan.MONTHLY,
                    onClick = { selectedPlan = PremiumPlan.MONTHLY },
                    modifier = Modifier.weight(1f),
                )
                PremiumPlanCard(
                    plan = PremiumPlan.YEARLY,
                    selected = selectedPlan == PremiumPlan.YEARLY,
                    onClick = { selectedPlan = PremiumPlan.YEARLY },
                    modifier = Modifier.weight(1f),
                )
            }
            Button(
                onClick = { onSubscribe(selectedPlan.name) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Lanjutkan dengan ${selectedPlan.label}")
            }
            Text(
                "Pembayaran dan entitlement diproses oleh data/billing layer, bukan UI ini.",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Text("Semua benefit Premium tersedia sesuai entitlement akun.", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun PremiumBenefit(title: String, description: String, enabled: Boolean) {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (enabled) Icons.Outlined.CheckCircle else Icons.Outlined.Lock,
                null,
                tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PremiumPlanCard(
    plan: PremiumPlan,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = .12f) else MaterialTheme.colorScheme.surface,
        tonalElevation = if (selected) 2.dp else 0.dp,
        onClick = onClick,
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(plan.label, fontWeight = FontWeight.Bold)
            Text(plan.period, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(if (plan == PremiumPlan.YEARLY) "Pilihan tahunan" else "Fleksibel", fontSize = 11.sp)
        }
    }
}
