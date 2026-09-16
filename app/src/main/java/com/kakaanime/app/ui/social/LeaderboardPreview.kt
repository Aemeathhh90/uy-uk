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
                    Text("Swipe untuk kategori lain", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                demoPreviewEntries(slide).forEach { entry ->
                                    Surface(Modifier.width(94.dp), RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f)) {
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

private enum class LeaderboardSlide(val title: String) {
    SUPPORTER("Supporter • Support Points"),
    WATCHER_SOLO("Watcher • Solo"),
    WATCHER_TOGETHER("Watcher • Watch Together"),
    ACHIEVEMENT("Achievement • Achievement dibuka"),
}

private fun demoPreviewEntries(slide: LeaderboardSlide): List<LeaderboardEntryUi> {
    val values = when (slide) {
        LeaderboardSlide.SUPPORTER -> listOf("12.450 SP", "11.820 SP", "10.975 SP")
        LeaderboardSlide.WATCHER_SOLO -> listOf("182j 34m", "176j 12m", "169j 48m")
        LeaderboardSlide.WATCHER_TOGETHER -> listOf("94j 18m", "88j 42m", "81j 27m")
        LeaderboardSlide.ACHIEVEMENT -> listOf("87 achievement", "82 achievement", "79 achievement")
    }
    return listOf("Aki", "Rin", "Yuki").mapIndexed { index, name ->
        LeaderboardEntryUi(index + 1, name, values[index])
    }
}
