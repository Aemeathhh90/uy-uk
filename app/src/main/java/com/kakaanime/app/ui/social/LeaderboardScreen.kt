package com.kakaanime.app.ui.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
                    Text("Semua kategori komunitas dalam satu tempat.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Outlined.EmojiEvents, null, tint = MaterialTheme.colorScheme.primary)
            }
        }

        item {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                LeaderboardPeriod.entries.forEachIndexed { index, period ->
                    SegmentedButton(
                        selected = state.period == period,
                        onClick = { onPeriodSelected(period) },
                        shape = SegmentedButtonDefaults.itemShape(index, LeaderboardPeriod.entries.size),
                        icon = {},
                    ) { Text(period.label, fontSize = 10.sp) }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Watcher", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text("Pilih sumber waktu tonton yang ingin dibandingkan.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    LeaderboardWatcherMode.entries.forEachIndexed { index, mode ->
                        SegmentedButton(
                            selected = state.watcherMode == mode,
                            onClick = { onWatcherModeSelected(mode) },
                            shape = SegmentedButtonDefaults.itemShape(index, LeaderboardWatcherMode.entries.size),
                            icon = {},
                        ) {
                            Icon(
                                if (mode == LeaderboardWatcherMode.SOLO) Icons.Outlined.Person else Icons.Outlined.Groups,
                                null,
                                Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(mode.label, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        item { LeaderboardOverview(state) }

        item {
            LeaderboardSection(
                title = "Supporter",
                subtitle = "Support Points (SP) yang tercatat.",
                icon = Icons.Outlined.Star,
                entries = demoSectionEntries(LeaderboardCategory.SUPPORTER, state.watcherMode),
                myRank = 54,
                myValue = "${state.myStats.supportPoints} SP",
                previousRank = 61,
            )
        }

        item {
            LeaderboardSection(
                title = "Watcher • ${state.watcherMode.label}",
                subtitle = if (state.watcherMode == LeaderboardWatcherMode.SOLO) {
                    "Waktu tonton Solo dari pemutaran aktif yang terverifikasi."
                } else {
                    "Waktu tonton Watch Together dihitung per akun."
                },
                icon = if (state.watcherMode == LeaderboardWatcherMode.SOLO) Icons.Outlined.Timer else Icons.Outlined.Groups,
                entries = demoSectionEntries(LeaderboardCategory.WATCHER, state.watcherMode),
                myRank = if (state.watcherMode == LeaderboardWatcherMode.SOLO) 128 else 96,
                myValue = if (state.watcherMode == LeaderboardWatcherMode.SOLO) state.myStats.soloWatchTime else state.myStats.watchTogetherTime,
                previousRank = if (state.watcherMode == LeaderboardWatcherMode.SOLO) 135 else 101,
            )
        }

        item {
            LeaderboardSection(
                title = "Achievement",
                subtitle = "Jumlah achievement yang sudah dibuka.",
                icon = Icons.Outlined.EmojiEvents,
                entries = demoSectionEntries(LeaderboardCategory.ACHIEVEMENT, state.watcherMode),
                myRank = 37,
                myValue = "${state.myStats.achievements} achievement",
                previousRank = 41,
            )
        }
    }
}

@Composable
private fun LeaderboardOverview(state: LeaderboardUiState) {
    Surface(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Ringkasan kamu", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatMini("Solo", state.myStats.soloWatchTime, Icons.Outlined.Timer, Modifier.weight(1f))
                StatMini("Together", state.myStats.watchTogetherTime, Icons.Outlined.Groups, Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatMini("Support Points", "${state.myStats.supportPoints} SP", Icons.Outlined.Star, Modifier.weight(1f))
                StatMini("Achievement", "${state.myStats.achievements}", Icons.Outlined.EmojiEvents, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun LeaderboardSection(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    entries: List<LeaderboardEntryUi>,
    myRank: Int,
    myValue: String,
    previousRank: Int?,
) {
    Surface(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(38.dp), CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(19.dp)) }
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(2.dp))
            entries.take(3).forEach { entry -> LeaderboardEntry(entry) }
            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .70f)) {
                Row(Modifier.padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Posisi kamu", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("#$myRank", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(myValue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        previousRank?.let {
                            val delta = it - myRank
                            if (delta != 0) Text(
                                if (delta > 0) "▲ $delta" else "▼ ${-delta}",
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMini(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Surface(modifier, RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = .55f)) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(17.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun LeaderboardEntry(entry: LeaderboardEntryUi) {
    val container = if (entry.isSelf) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface.copy(alpha = .55f)
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), color = container) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#${entry.rank}", Modifier.width(42.dp), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            Surface(Modifier.size(36.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                Box(contentAlignment = Alignment.Center) { Text(entry.username.take(2).uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) {
                Text(entry.username, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                if (entry.subtitle.isNotBlank()) Text(entry.subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(entry.valueLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun demoSectionEntries(category: LeaderboardCategory, mode: LeaderboardWatcherMode): List<LeaderboardEntryUi> {
    val values = when (category) {
        LeaderboardCategory.SUPPORTER -> listOf("12.450 SP", "11.820 SP", "10.975 SP")
        LeaderboardCategory.WATCHER -> if (mode == LeaderboardWatcherMode.SOLO) {
            listOf("182j 34m", "176j 12m", "169j 48m")
        } else {
            listOf("94j 18m", "88j 42m", "81j 27m")
        }
        LeaderboardCategory.ACHIEVEMENT -> listOf("87 achievement", "82 achievement", "79 achievement")
    }
    return listOf("Aki", "Rin", "Yuki").mapIndexed { index, name ->
        LeaderboardEntryUi(index + 1, name, values[index], "Top performer")
    }
}
