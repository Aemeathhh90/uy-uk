package com.kakaanime.app.ui.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SocialLeaderboardPreview(
    state: LeaderboardUiState,
    onViewAll: () -> Unit,
) {
    val slides = listOf(
        LeaderboardSlide.SUPPORTER,
        LeaderboardSlide.WATCHER_SOLO,
        LeaderboardSlide.WATCHER_TOGETHER,
        LeaderboardSlide.ACHIEVEMENT,
    )
    val pagerState = rememberLazyListState()
    var page by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4500)
            page = (page + 1) % slides.size
            pagerState.animateScrollToItem(page)
        }
    }

    LaunchedEffect(pagerState.firstVisibleItemIndex) {
        val visiblePage = pagerState.firstVisibleItemIndex.coerceIn(0, slides.lastIndex)
        if (visiblePage != page) page = visiblePage
    }

    Surface(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .34f),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.EmojiEvents, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text("Leaderboard", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Top 1–3 + geser untuk Top 4–10", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = onViewAll) { Text("Lihat Semua", fontSize = 10.sp) }
            }
            Spacer(Modifier.height(10.dp))
            LazyRow(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(end = 4.dp),
            ) {
                items(slides, key = { it.name }) { slide ->
                    Surface(
                        Modifier.fillParentMaxWidth(),
                        RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = .65f),
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(slide.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                demoPreviewEntries(slide).take(3).forEach { entry ->
                                    PreviewTopEntry(entry, Modifier.weight(1f))
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Top 4–10", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(5.dp))
                                Text("Geser →", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(Modifier.height(6.dp))
                            val railState = rememberLazyListState()
                            LazyRow(
                                state = railState,
                                horizontalArrangement = Arrangement.spacedBy(7.dp),
                                contentPadding = PaddingValues(end = 8.dp),
                            ) {
                                items(demoPreviewEntries(slide).drop(3), key = { it.rank }) { entry ->
                                    Surface(
                                        Modifier.width(92.dp),
                                        RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f),
                                    ) {
                                        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("#${entry.rank}", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                                            Spacer(Modifier.height(4.dp))
                                            Surface(Modifier.size(30.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(entry.username.take(2).uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                            Spacer(Modifier.height(4.dp))
                                            Text(entry.username, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                            Text(entry.valueLabel, fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(9.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                slides.indices.forEach { index ->
                    Text(
                        if (index == page) "●" else "○",
                        fontSize = 10.sp,
                        color = if (index == page) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (index < slides.lastIndex) Spacer(Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
private fun PreviewTopEntry(entry: LeaderboardEntryUi, modifier: Modifier = Modifier) {
    Surface(modifier, RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f)) {
        Column(Modifier.padding(9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("#${entry.rank}", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(5.dp))
            Surface(Modifier.size(36.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                Box(contentAlignment = Alignment.Center) {
                    Text(entry.username.take(2).uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(5.dp))
            Text(entry.username, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(entry.valueLabel, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

private enum class LeaderboardSlide(val title: String) {
    SUPPORTER("Supporter • Support Points"),
    WATCHER_SOLO("Watcher • Solo"),
    WATCHER_TOGETHER("Watcher • Watch Together"),
    ACHIEVEMENT("Achievement • Achievement dibuka"),
}

private fun demoPreviewEntries(slide: LeaderboardSlide): List<LeaderboardEntryUi> {
    val values = when (slide) {
        LeaderboardSlide.SUPPORTER -> listOf("12.450 SP", "11.820 SP", "10.975 SP", "9.640 SP", "8.920 SP", "8.410 SP", "7.980 SP", "7.540 SP", "7.120 SP", "6.880 SP")
        LeaderboardSlide.WATCHER_SOLO -> listOf("182j 34m", "176j 12m", "169j 48m", "158j 20m", "151j 44m", "147j 10m", "142j 36m", "138j 22m", "134j 18m", "130j 05m")
        LeaderboardSlide.WATCHER_TOGETHER -> listOf("94j 18m", "88j 42m", "81j 27m", "76j 11m", "71j 36m", "68j 04m", "64j 50m", "61j 18m", "58j 42m", "55j 20m")
        LeaderboardSlide.ACHIEVEMENT -> listOf("87 achievement", "82 achievement", "79 achievement", "75 achievement", "72 achievement", "69 achievement", "65 achievement", "62 achievement", "59 achievement", "56 achievement")
    }
    val names = listOf("Aki", "Rin", "Yuki", "Akira", "Mika", "Hana", "Sora", "Kiyo", "Nami", "Ren")
    return names.mapIndexed { index, name -> LeaderboardEntryUi(index + 1, name, values[index]) }
}
