package com.kakaanime.app.ui.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Groups
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
                    Text("Empat kategori, satu halaman peringkat.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text("Watcher", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Mode waktu tonton yang dipakai untuk bagian Watcher.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    LeaderboardWatcherMode.entries.forEachIndexed { index, mode ->
                        SegmentedButton(
                            selected = state.watcherMode == mode,
                            onClick = { onWatcherModeSelected(mode) },
                            shape = SegmentedButtonDefaults.itemShape(index, LeaderboardWatcherMode.entries.size),
                            icon = {},
                        ) {
                            Icon(
                                if (mode == LeaderboardWatcherMode.SOLO) Icons.Outlined.Timer else Icons.Outlined.Groups,
                                null,
                                Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(mode.label, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        item { LeaderboardOverview(state) }

        item {
            LeaderboardPodiumSection(
                title = "Watcher • Solo",
                subtitle = "Waktu tonton Solo yang terverifikasi.",
                icon = Icons.Outlined.Timer,
                entries = demoFullEntries(FullLeaderboardCategory.WATCHER_SOLO),
                myRank = 128,
                myValue = state.myStats.soloWatchTime,
                previousRank = 135,
            )
        }

        item {
            LeaderboardPodiumSection(
                title = "Watcher • Watch Together",
                subtitle = "Waktu tonton Watch Together dihitung per akun.",
                icon = Icons.Outlined.Groups,
                entries = demoFullEntries(FullLeaderboardCategory.WATCHER_TOGETHER),
                myRank = 96,
                myValue = state.myStats.watchTogetherTime,
                previousRank = 101,
            )
        }

        item {
            LeaderboardPodiumSection(
                title = "Supporter",
                subtitle = "Support Points (SP) yang tercatat.",
                icon = Icons.Outlined.Star,
                entries = demoFullEntries(FullLeaderboardCategory.SUPPORTER),
                myRank = 54,
                myValue = "${state.myStats.supportPoints} SP",
                previousRank = 61,
            )
        }

        item {
            LeaderboardPodiumSection(
                title = "Achievement",
                subtitle = "Jumlah achievement yang sudah dibuka.",
                icon = Icons.Outlined.EmojiEvents,
                entries = demoFullEntries(FullLeaderboardCategory.ACHIEVEMENT),
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
private fun LeaderboardPodiumSection(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    entries: List<LeaderboardEntryUi>,
    myRank: Int,
    myValue: String,
    previousRank: Int?,
) {
    val topThree = entries.take(3)
    val remaining = entries.drop(3)

    Surface(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(38.dp), CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(19.dp))
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(8.dp))
            Podium(topThree)
            Spacer(Modifier.height(8.dp))

            Text("Top 4–10", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            remaining.forEach { entry -> LeaderboardEntry(entry) }

            Surface(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .70f),
            ) {
                Row(Modifier.padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Posisi kamu", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("#$myRank", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(myValue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        previousRank?.let {
                            val delta = it - myRank
                            if (delta != 0) {
                                Text(
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
}

@Composable
private fun Podium(entries: List<LeaderboardEntryUi>) {
    val first = entries.getOrNull(0)
    val second = entries.getOrNull(1)
    val third = entries.getOrNull(2)

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        PodiumEntry(second, Modifier.weight(1f), podiumHeight = 112.dp, medal = "🥈")
        PodiumEntry(first, Modifier.weight(1f), podiumHeight = 132.dp, medal = "🥇")
        PodiumEntry(third, Modifier.weight(1f), podiumHeight = 100.dp, medal = "🥉")
    }
}

@Composable
private fun PodiumEntry(
    entry: LeaderboardEntryUi?,
    modifier: Modifier,
    podiumHeight: androidx.compose.ui.unit.Dp,
    medal: String,
) {
    if (entry == null) return
    Surface(
        modifier.height(podiumHeight),
        RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = .70f),
    ) {
        Column(
            Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(medal, fontSize = 18.sp)
            Surface(Modifier.size(38.dp), CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Box(contentAlignment = Alignment.Center) {
                    Text(entry.username.take(2).uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(5.dp))
            Text("#${entry.rank}", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            Text(entry.username, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(entry.valueLabel, fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
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
            Surface(Modifier.size(34.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                Box(contentAlignment = Alignment.Center) {
                    Text(entry.username.take(2).uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) {
                Text(entry.username, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(entry.valueLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private enum class FullLeaderboardCategory {
    WATCHER_SOLO,
    WATCHER_TOGETHER,
    SUPPORTER,
    ACHIEVEMENT,
}

private fun demoFullEntries(category: FullLeaderboardCategory): List<LeaderboardEntryUi> {
    val values = when (category) {
        FullLeaderboardCategory.SUPPORTER -> listOf("12.450 SP", "11.820 SP", "10.975 SP", "9.640 SP", "8.920 SP", "8.410 SP", "7.980 SP", "7.540 SP", "7.120 SP", "6.880 SP")
        FullLeaderboardCategory.WATCHER_SOLO -> listOf("182j 34m", "176j 12m", "169j 48m", "158j 20m", "151j 44m", "147j 10m", "142j 36m", "138j 22m", "134j 18m", "130j 05m")
        FullLeaderboardCategory.WATCHER_TOGETHER -> listOf("94j 18m", "88j 42m", "81j 27m", "76j 11m", "71j 36m", "68j 04m", "64j 50m", "61j 18m", "58j 42m", "55j 20m")
        FullLeaderboardCategory.ACHIEVEMENT -> listOf("87 achievement", "82 achievement", "79 achievement", "75 achievement", "72 achievement", "69 achievement", "65 achievement", "62 achievement", "59 achievement", "56 achievement")
    }
    val names = listOf("Aki", "Rin", "Yuki", "Akira", "Mika", "Hana", "Sora", "Kiyo", "Nami", "Ren")
    return names.mapIndexed { index, name ->
        LeaderboardEntryUi(index + 1, name, values[index], "Top performer")
    }
}
