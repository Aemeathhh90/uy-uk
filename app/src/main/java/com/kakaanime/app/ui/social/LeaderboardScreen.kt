package com.kakaanime.app.ui.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LeaderboardScreen(
    state: LeaderboardUiState,
    onBack: () -> Unit,
    onCategorySelected: (LeaderboardCategory) -> Unit = {},
    onWatcherModeSelected: (LeaderboardWatcherMode) -> Unit = {},
    onPeriodSelected: (LeaderboardPeriod) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 10.dp, 16.dp, 116.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
                Column(Modifier.weight(1f)) {
                    Text("Leaderboard", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Lihat progres komunitas KakaAnime.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Outlined.EmojiEvents, null, tint = MaterialTheme.colorScheme.primary)
            }
        }
        item {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                LeaderboardCategory.entries.forEachIndexed { index, category ->
                    SegmentedButton(selected = state.category == category, onClick = { onCategorySelected(category) }, shape = SegmentedButtonDefaults.itemShape(index, LeaderboardCategory.entries.size), icon = {}) {
                        Text(category.label, fontSize = 11.sp)
                    }
                }
            }
        }
        if (state.category == LeaderboardCategory.WATCHER) {
            item {
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    LeaderboardWatcherMode.entries.forEachIndexed { index, mode ->
                        SegmentedButton(selected = state.watcherMode == mode, onClick = { onWatcherModeSelected(mode) }, shape = SegmentedButtonDefaults.itemShape(index, LeaderboardWatcherMode.entries.size), icon = {}) {
                            Icon(if (mode == LeaderboardWatcherMode.SOLO) Icons.Outlined.Person else Icons.Outlined.Groups, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(5.dp))
                            Text(mode.label, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
        item {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                LeaderboardPeriod.entries.forEachIndexed { index, period ->
                    SegmentedButton(selected = state.period == period, onClick = { onPeriodSelected(period) }, shape = SegmentedButtonDefaults.itemShape(index, LeaderboardPeriod.entries.size), icon = {}) {
                        Text(period.label, fontSize = 10.sp)
                    }
                }
            }
        }
        item { MyLeaderboardStats(state) }
        items(state.entries, key = { "${it.rank}-${it.username}" }) { entry -> LeaderboardEntry(entry) }
    }
}

@Composable
private fun MyLeaderboardStats(state: LeaderboardUiState) {
    val valueLabel = when (state.category) {
        LeaderboardCategory.WATCHER -> state.myValueLabel
        LeaderboardCategory.SUPPORTER -> "${state.myStats.supportPoints} SP"
        LeaderboardCategory.ACHIEVEMENT -> "${state.myStats.achievements} achievement"
    }
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.primaryContainer) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Posisi kamu", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(if (state.myRank > 0) "#${state.myRank}" else "Belum berperingkat", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(valueLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    state.previousRank?.let { previous ->
                        val delta = previous - state.myRank
                        if (delta != 0) Text(if (delta > 0) "▲ $delta" else "▼ ${-delta}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatMini("Solo", state.myStats.soloWatchTime, Icons.Outlined.Timer, Modifier.weight(1f))
                StatMini("Together", state.myStats.watchTogetherTime, Icons.Outlined.Groups, Modifier.weight(1f))
                StatMini("SP", "${state.myStats.supportPoints}", Icons.Outlined.Star, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatMini(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Surface(modifier, RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = .55f)) {
        Column(Modifier.padding(9.dp)) {
            Icon(icon, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun LeaderboardEntry(entry: LeaderboardEntryUi) {
    val container = if (entry.isSelf) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .35f)
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), color = container) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#${entry.rank}", Modifier.width(48.dp), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
            Surface(Modifier.size(40.dp), CircleShape, color = MaterialTheme.colorScheme.surface) {
                Box(contentAlignment = Alignment.Center) { Text(entry.username.take(2).uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(entry.username, fontWeight = FontWeight.SemiBold)
                if (entry.subtitle.isNotBlank()) Text(entry.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(entry.valueLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
