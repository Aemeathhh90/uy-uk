package com.kakaanime.app.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kakaanime.app.ui.home.HomeAnimeUi

@Composable
fun CalendarScreen(
    state: CalendarUiState,
    onAnimeClick: (HomeAnimeUi) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    var selectedKey by remember(state.selectedDayKey, state.days) {
        mutableStateOf(state.selectedDayKey.ifBlank { state.days.firstOrNull()?.key.orEmpty() })
    }
    val selectedDay = state.days.firstOrNull { it.key == selectedKey }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 116.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Schedule", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        "Track upcoming anime episodes",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Outlined.Search, contentDescription = "Cari jadwal")
                }
                IconButton(onClick = onMoreClick) {
                    Icon(Icons.Outlined.MoreVert, contentDescription = "Menu jadwal")
                }
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 2.dp),
            ) {
                itemsIndexed(state.days) { _, day ->
                    ScheduleDayChip(
                        day = day,
                        selected = selectedKey == day.key,
                        onClick = { selectedKey = day.key },
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    selectedDay?.let { if (it.isToday) "Today · ${it.dateLabel}" else it.label }
                        ?: "Schedule",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    "${selectedDay?.episodes?.size ?: 0} episodes",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (selectedDay == null || selectedDay.episodes.isEmpty()) {
            item { EmptySchedule() }
        } else {
            itemsIndexed(
                selectedDay.episodes,
                key = { _, item -> "${selectedDay.key}-${item.anime.id}-${item.episode}" },
            ) { index, item ->
                ScheduleTimelineItem(
                    item = item,
                    showConnector = index < selectedDay.episodes.lastIndex,
                    onClick = { onAnimeClick(item.anime) },
                )
            }
        }

        if (state.updates.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("New Updates", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            items(state.updates.take(8), key = { "update-${it.anime.id}-${it.episode}" }) { item ->
                ScheduleUpdateRow(item, onClick = { onAnimeClick(item.anime) })
            }
        }
    }
}

@Composable
private fun ScheduleDayChip(day: CalendarDayUi, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.width(78.dp).height(74.dp),
        shape = RoundedCornerShape(15.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .38f),
        tonalElevation = if (selected) 3.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                day.label.take(3).uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(2.dp))
            Text(day.dateLabel, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            if (day.isToday) {
                Text("Today", fontSize = 9.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            } else {
                Spacer(Modifier.height(11.dp))
            }
        }
    }
}

@Composable
private fun ScheduleTimelineItem(item: CalendarEpisodeUi, showConnector: Boolean, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.width(52.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                item.timeLabel.ifBlank { "--:--" },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 18.dp),
            )
            Spacer(Modifier.height(7.dp))
            Box(Modifier.size(9.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
            if (showConnector) {
                Box(
                    Modifier.width(1.dp).height(102.dp).background(MaterialTheme.colorScheme.outlineVariant),
                )
            }
        }
        Spacer(Modifier.width(9.dp))
        ScheduleEpisodeCard(item, onClick)
    }
}

@Composable
private fun ScheduleEpisodeCard(item: CalendarEpisodeUi, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .38f),
    ) {
        Row(Modifier.padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(11.dp), modifier = Modifier.size(width = 66.dp, height = 92.dp)) {
                if (item.anime.posterUrl.isNullOrBlank()) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(item.anime.title.take(2).uppercase(), fontWeight = FontWeight.ExtraBold)
                    }
                } else {
                    AsyncImage(
                        model = item.anime.posterUrl,
                        contentDescription = item.anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(item.anime.title, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                Text(
                    "Episode ${item.episode}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    "TV  ·  ★ ${item.anime.rating}  ·  24m",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    item.anime.genre.ifBlank { item.anime.status },
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = .14f),
                    ) {
                        Text(
                            item.statusLabel,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (item.countdownLabel.isNotBlank()) {
                        Spacer(Modifier.width(7.dp))
                        Text(item.countdownLabel, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleUpdateRow(item: CalendarEpisodeUi, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .3f),
    ) {
        Row(Modifier.padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("EP ${item.episode}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(10.dp))
            Text(item.anime.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(item.statusLabel, fontSize = 9.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun EmptySchedule() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .34f),
    ) {
        Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(7.dp))
            Text("Belum ada episode", fontWeight = FontWeight.SemiBold)
            Text("Cek hari lain untuk jadwal berikutnya.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
