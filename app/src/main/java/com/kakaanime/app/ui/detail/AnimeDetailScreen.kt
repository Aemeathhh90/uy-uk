package com.kakaanime.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AnimeDetailScreen(
    anime: AnimeDetailUi,
    episodes: List<EpisodeUi>,
    seasons: List<SeasonUi> = emptyList(),
    isFavorite: Boolean = false,
    onBack: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    onEpisodeClick: (EpisodeUi) -> Unit = {},
    onSeasonSelected: (SeasonUi) -> Unit = {},
    onEpisodeGate: (EpisodeUi) -> Unit = {},
) {
    var tab by remember { mutableStateOf(DetailTab.INFO) }
    var viewMode by remember { mutableStateOf(EpisodeViewMode.LIST) }
    var filter by remember { mutableStateOf(EpisodeFilter.ALL) }
    var query by remember { mutableStateOf("") }
    var seasonMenu by remember { mutableStateOf(false) }

    val visibleEpisodes = remember(episodes, filter, query) {
        episodes.filter { episode ->
            val filterMatch = when (filter) {
                EpisodeFilter.ALL -> true
                EpisodeFilter.NEW -> episode.isNew
                EpisodeFilter.UNWATCHED -> !episode.isWatched
            }
            val queryMatch = query.isBlank() || episode.number.toString().contains(query)
            filterMatch && queryMatch
        }
    }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
            Text(anime.title, Modifier.weight(1f), maxLines = 1, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                )
            }
        }

        Surface(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .32f),
        ) {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(82.dp).clip(RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    if (anime.posterUrl != null) {
                        AsyncImage(anime.posterUrl, anime.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Outlined.Movie, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(anime.title, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, maxLines = 2)
                    Text("${anime.status.uppercase()} • ${anime.year} • ${anime.type}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Star, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(3.dp))
                        Text(anime.rating, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Box(Modifier.weight(1f)) {
            when (tab) {
                DetailTab.INFO -> InfoContent(anime, episodes.size, onStart = { episodes.firstOrNull()?.let(onEpisodeClick) })
                DetailTab.EPISODES -> EpisodeContent(
                    episodes = visibleEpisodes,
                    seasons = seasons,
                    viewMode = viewMode,
                    filter = filter,
                    query = query,
                    seasonMenu = seasonMenu,
                    onViewModeChange = { viewMode = it },
                    onFilterChange = { filter = it },
                    onQueryChange = { query = it.filter(Char::isDigit).take(5) },
                    onSeasonMenuChange = { seasonMenu = it },
                    onSeasonSelected = onSeasonSelected,
                    onEpisodeClick = { episode -> if (episode.isLocked) onEpisodeGate(episode) else onEpisodeClick(episode) },
                )
            }
        }

        Row(Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 34.dp), verticalAlignment = Alignment.CenterVertically) {
            DetailTabButton(tab == DetailTab.INFO, Icons.Outlined.Info, "INFO") { tab = DetailTab.INFO }
            DetailTabButton(tab == DetailTab.EPISODES, Icons.Outlined.Movie, "EPISODES") { tab = DetailTab.EPISODES }
        }
    }
}

@Composable
private fun InfoContent(anime: AnimeDetailUi, episodeCount: Int, onStart: () -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(anime.genre, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                Text("Tentang Anime", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(anime.description.ifBlank { "Belum ada deskripsi untuk anime ini." }, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp, maxLines = 6)
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Episode", episodeCount.toString(), Modifier.weight(1f))
                StatCard("Studio", anime.studio, Modifier.weight(1f))
                StatCard("Season", anime.season, Modifier.weight(1f))
            }
        }
        item {
            Button(onClick = onStart, enabled = episodeCount > 0, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(15.dp)) {
                Icon(Icons.Filled.PlayArrow, null)
                Spacer(Modifier.width(8.dp))
                Text(if (episodeCount > 0) "Mulai Nonton" else "Episode Belum Tersedia", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EpisodeContent(
    episodes: List<EpisodeUi>,
    seasons: List<SeasonUi>,
    viewMode: EpisodeViewMode,
    filter: EpisodeFilter,
    query: String,
    seasonMenu: Boolean,
    onViewModeChange: (EpisodeViewMode) -> Unit,
    onFilterChange: (EpisodeFilter) -> Unit,
    onQueryChange: (String) -> Unit,
    onSeasonMenuChange: (Boolean) -> Unit,
    onSeasonSelected: (SeasonUi) -> Unit,
    onEpisodeClick: (EpisodeUi) -> Unit,
) {
    val selectedSeason = seasons.firstOrNull()
    val controls: @Composable () -> Unit = {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            if (seasons.size > 1) {
                Box {
                    OutlinedButton(onClick = { onSeasonMenuChange(true) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) {
                        Text(selectedSeason?.label ?: "Pilih Season", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        Text("▼", fontSize = 12.sp)
                    }
                    DropdownMenu(expanded = seasonMenu, onDismissRequest = { onSeasonMenuChange(false) }) {
                        seasons.forEach { season ->
                            DropdownMenuItem(text = { Text(season.label) }, onClick = { onSeasonMenuChange(false); onSeasonSelected(season) })
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Episodes", Modifier.weight(1f), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                IconButton(onClick = { onViewModeChange(EpisodeViewMode.LIST) }) { Icon(Icons.Filled.List, "List", tint = if (viewMode == EpisodeViewMode.LIST) MaterialTheme.colorScheme.primary else LocalContentColor.current) }
                IconButton(onClick = { onViewModeChange(EpisodeViewMode.GRID) }) { Icon(Icons.Filled.GridView, "Grid", tint = if (viewMode == EpisodeViewMode.GRID) MaterialTheme.colorScheme.primary else LocalContentColor.current) }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                items(EpisodeFilter.entries.toList()) { option -> FilterChip(selected = filter == option, onClick = { onFilterChange(option) }, label = { Text(option.label, fontSize = 11.sp) }) }
            }
            OutlinedTextField(value = query, onValueChange = onQueryChange, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(15.dp), leadingIcon = { Icon(Icons.Outlined.Search, null) }, placeholder = { Text("Cari episode...") })
        }
    }

    if (viewMode == EpisodeViewMode.GRID) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) { controls() }
            if (episodes.isEmpty()) item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) { EmptyEpisodes() }
            else items(episodes, key = { it.number }) { EpisodeGridCard(it, onEpisodeClick) }
        }
    } else {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            item { controls() }
            if (episodes.isEmpty()) item { EmptyEpisodes() }
            else items(episodes, key = { it.number }) { EpisodeListCard(it, onEpisodeClick) }
        }
    }
}

@Composable
private fun EpisodeListCard(episode: EpisodeUi, onClick: (EpisodeUi) -> Unit) {
    Surface(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { onClick(episode) }, RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .40f)) {
        Row(Modifier.padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(96.dp, 62.dp).clip(RoundedCornerShape(11.dp)), contentAlignment = Alignment.Center) {
                if (episode.thumbnailUrl != null) AsyncImage(episode.thumbnailUrl, episode.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = if (episode.isWatched) .16f else .46f)))
                Icon(if (episode.isWatched) Icons.Filled.PlayArrow else Icons.Outlined.Lock, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Episode ${episode.number}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(episode.title, fontWeight = FontWeight.SemiBold, maxLines = 2)
                if (episode.isNew) Text("NEW", fontSize = 9.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EpisodeGridCard(episode: EpisodeUi, onClick: (EpisodeUi) -> Unit) {
    Surface(Modifier.fillMaxWidth().clickable { onClick(episode) }, RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .38f)) {
        Column {
            Box(Modifier.fillMaxWidth().aspectRatio(1.55f).clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)), contentAlignment = Alignment.Center) {
                if (episode.thumbnailUrl != null) AsyncImage(episode.thumbnailUrl, episode.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = if (episode.isWatched) .15f else .46f)))
                Icon(if (episode.isWatched) Icons.Filled.PlayArrow else Icons.Outlined.Lock, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
            }
            Column(Modifier.padding(10.dp)) {
                Text("Episode ${episode.number}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(episode.title, fontWeight = FontWeight.SemiBold, maxLines = 2)
                if (episode.isNew) Text("NEW", fontSize = 9.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier) {
    Surface(modifier, RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .32f)) {
        Column(Modifier.padding(11.dp)) { Text(title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 2) }
    }
}

@Composable
private fun EmptyEpisodes() {
    Surface(Modifier.fillMaxWidth().padding(16.dp), RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .35f)) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.Movie, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text("Episode tidak ditemukan", fontWeight = FontWeight.Bold)
            Text("Coba ubah filter atau nomor episode.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun RowScope.DetailTabButton(selected: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(Modifier.weight(1f).fillMaxHeight().clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(Modifier.fillMaxWidth(.72f).height(3.dp).clip(RoundedCornerShape(99.dp)).background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background))
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(20.dp), tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(5.dp))
            Text(label, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
