package com.kakaanime.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kakaanime.app.ui.update.UpdateDialog
import com.kakaanime.app.ui.update.UpdateUiState

@Composable
fun HomeV1Screen(
    state: HomeUiState,
    onAnimeClick: (HomeAnimeUi) -> Unit = {},
    onContinueWatchingClick: (ContinueWatchingUi) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onDiamondClick: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onWatchTogetherClick: () -> Unit = {},
    updateState: UpdateUiState = UpdateUiState(),
    onUpdateClick: () -> Unit = {},
) {
    var query by remember(state.searchQuery) { mutableStateOf(state.searchQuery) }
    var filter by remember(state.selectedFilter) { mutableStateOf(state.selectedFilter) }
    var showFilters by remember { mutableStateOf(false) }
    var showUpdate by remember(updateState.isUpdateAvailable) { mutableStateOf(updateState.isUpdateAvailable) }

    val filtered = remember(state.anime, query, filter) {
        state.anime.filter { anime ->
            val matchesQuery = query.isBlank() || anime.title.contains(query, true) || anime.genre.contains(query, true)
            val matchesFilter = when (filter) {
                HomeFilter.ALL -> true
                HomeFilter.ONGOING -> anime.status.equals("Ongoing", true)
                HomeFilter.FINISHED -> anime.status.equals("Finished", true) || anime.status.equals("Tamat", true)
            }
            matchesQuery && matchesFilter
        }
    }
    val featured = remember(state.featuredAnime, state.anime) {
        (state.featuredAnime.ifEmpty { state.anime }).distinctBy { it.id }.take(5)
    }
    val newEpisodes = remember(state.anime) {
        state.anime.filter { it.isNew }.ifEmpty { state.anime.sortedByDescending { it.latestEpisode } }
    }
    val ongoing = remember(state.anime) { state.anime.filter { it.status.equals("Ongoing", true) } }
    val finished = remember(state.anime) {
        state.anime.filter { it.status.equals("Finished", true) || it.status.equals("Tamat", true) }
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                HomeTopBar(
                    query = query,
                    onQueryChange = { query = it },
                    showFilters = showFilters,
                    onFilterClick = { showFilters = !showFilters },
                    onNotificationsClick = onNotificationsClick,
                )
            }
            if (showFilters) {
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(HomeFilter.entries) { option ->
                            FilterChip(
                                selected = filter == option,
                                onClick = { filter = option },
                                label = { Text(option.label) },
                            )
                        }
                    }
                }
            }
            item {
                HomeProfileStrip(
                    username = state.username,
                    avatarUrl = state.avatarUrl,
                    diamonds = state.diamonds,
                    isPremium = state.isPremium,
                    onProfileClick = onProfileClick,
                    onDiamondClick = onDiamondClick,
                    onPremiumClick = onPremiumClick,
                    onWatchTogetherClick = onWatchTogetherClick,
                )
            }
            if (query.isBlank()) {
                if (featured.isNotEmpty()) item { HomeFeaturedSection(featured, onAnimeClick) }
                if (!state.isPremium) item { HomePremiumBanner(onPremiumClick) }
                if (state.continueWatching.isNotEmpty()) item { HomeContinueSection(state.continueWatching, onContinueWatchingClick) }
                item { HomeAnimeSection("New Episode", newEpisodes, onAnimeClick) }
                item { HomeAnimeSection("Ongoing", ongoing, onAnimeClick) }
                item { HomeAnimeSection("Anime Tamat", finished, onAnimeClick) }
            } else {
                item {
                    if (filtered.isEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .45f),
                        ) {
                            Text("Anime tidak ditemukan", Modifier.padding(18.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        HomeAnimeSection("Hasil Pencarian", filtered, onAnimeClick)
                    }
                }
            }
        }

        if (showUpdate && updateState.isUpdateAvailable) {
            UpdateDialog(
                state = updateState,
                onLater = { showUpdate = false },
                onUpdate = {
                    showUpdate = false
                    onUpdateClick()
                },
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    showFilters: Boolean,
    onFilterClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("KakaAnime", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                Text("Watch Anime, Together.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                onClick = onNotificationsClick,
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .58f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Notifications, "Notifikasi")
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Outlined.Search, "Cari") },
                placeholder = { Text("Cari anime atau genre...") },
            )
            Surface(
                onClick = onFilterClick,
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (showFilters) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f),
            ) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Tune, "Filter") }
            }
        }
    }
}

@Composable
private fun HomeProfileStrip(
    username: String,
    avatarUrl: String?,
    diamonds: Int,
    isPremium: Boolean,
    onProfileClick: () -> Unit,
    onDiamondClick: () -> Unit,
    onPremiumClick: () -> Unit,
    onWatchTogetherClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .22f),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onProfileClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(Modifier.size(48.dp), CircleShape, color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
                    Box(contentAlignment = Alignment.Center) {
                        if (avatarUrl != null) {
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Foto profil",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Text("KA", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(username, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                        if (isPremium) {
                            Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .16f)) {
                                Text("PREMIUM", Modifier.padding(horizontal = 6.dp, vertical = 3.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    Text(if (isPremium) "Premium aktif" else "Akun Free", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HomeShortcut("◆", diamonds.toString(), "Key", Modifier.weight(1f), onDiamondClick)
                HomeShortcut("♛", if (isPremium) "1080p" else "Premium", if (isPremium) "Auto Skip" else "Upgrade", Modifier.weight(1f), onPremiumClick)
                HomeShortcut("••", "Together", "Nonton bareng", Modifier.weight(1f), onWatchTogetherClick)
            }
        }
    }
}

@Composable
private fun HomeShortcut(icon: String, title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Column(Modifier.padding(horizontal = 9.dp, vertical = 7.dp), verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(icon, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            Text(subtitle, fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

@Composable
private fun HomeFeaturedSection(featured: List<HomeAnimeUi>, onAnimeClick: (HomeAnimeUi) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        HomeSectionHeader("Featured")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 8.dp)) {
            items(featured, key = { "featured:${it.id}" }) { anime ->
                Surface(
                    onClick = { onAnimeClick(anime) },
                    modifier = Modifier.width(330.dp).height(224.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Box {
                        if (anime.posterUrl != null) {
                            AsyncImage(
                                model = anime.posterUrl,
                                contentDescription = anime.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            HomeArtwork(anime, Modifier.fillMaxSize())
                        }
                        Box(
                            Modifier.fillMaxSize().background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.scrim.copy(alpha = .02f),
                                        MaterialTheme.colorScheme.scrim.copy(alpha = .9f),
                                    )
                                )
                            )
                        )
                        Column(
                            Modifier.align(Alignment.BottomStart).padding(16.dp).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .92f)) {
                                Text("FEATURED", Modifier.padding(horizontal = 7.dp, vertical = 4.dp), fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
                            }
                            Text(anime.title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary, maxLines = 1)
                            Text(
                                "Ep ${anime.latestEpisode}  •  ★ ${anime.rating}  •  ${anime.status}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .86f),
                                maxLines = 1,
                            )
                        }
                        Surface(
                            onClick = { onAnimeClick(anime) },
                            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(40.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = .62f),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.PlayArrow, "Putar", tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            }
        }
        if (featured.size > 1) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                repeat(featured.size) { index ->
                    Box(
                        Modifier.padding(horizontal = 3.dp).size(if (index == 0) 16.dp else 5.dp, 5.dp).clip(CircleShape)
                            .background(if (index == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeArtwork(anime: HomeAnimeUi, modifier: Modifier) {
    Box(modifier.background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.surfaceVariant)))) {
        Text(
            anime.title.take(1).uppercase(),
            modifier = Modifier.align(Alignment.Center),
            fontSize = 64.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary.copy(alpha = .34f),
        )
        Text(
            anime.genre,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}

@Composable
private fun HomePremiumBanner(onPremiumClick: () -> Unit) {
    Surface(
        onClick = onPremiumClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .64f),
    ) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Premium", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                Text("1080p • Auto Skip • tanpa biaya Key", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("Upgrade", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun HomeSectionHeader(title: String) {
    Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
}

@Composable
private fun HomeContinueSection(entries: List<ContinueWatchingUi>, onClick: (ContinueWatchingUi) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        HomeSectionHeader("Lanjut Nonton")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 8.dp)) {
            items(entries, key = { "continue:${it.anime.id}:${it.episode}" }) { entry ->
                Column(Modifier.width(150.dp).clickable { onClick(entry) }) {
                    Box(Modifier.fillMaxWidth().height(178.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                        val image = entry.thumbnailUrl ?: entry.anime.posterUrl
                        if (image != null) {
                            AsyncImage(image, entry.anime.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        } else {
                            HomeArtwork(entry.anime, Modifier.fillMaxSize())
                        }
                        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.scrim.copy(alpha = .04f), MaterialTheme.colorScheme.scrim.copy(alpha = .8f)))))
                        Surface(Modifier.align(Alignment.TopStart).padding(8.dp), RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.scrim.copy(alpha = .72f)) {
                            Text("EP ${entry.episode}", Modifier.padding(horizontal = 7.dp, vertical = 4.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(9.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("${entry.progressPercent}%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                            Box(Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(3.dp)).background(MaterialTheme.colorScheme.onPrimary.copy(alpha = .24f))) {
                                Box(Modifier.fillMaxWidth((entry.progressPercent.coerceIn(0, 100) / 100f)).height(4.dp).background(MaterialTheme.colorScheme.primary))
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(entry.anime.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    Text(entry.episodeTitle ?: "Episode ${entry.episode}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun HomeAnimeSection(title: String, anime: List<HomeAnimeUi>, onAnimeClick: (HomeAnimeUi) -> Unit) {
    if (anime.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        HomeSectionHeader(title)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 8.dp)) {
            items(anime, key = { "anime:${it.id}:$title" }) { item ->
                val badge = when {
                    item.isNew -> "NEW EP"
                    item.status.equals("Ongoing", true) -> "ONGOING"
                    item.status.equals("Hiatus", true) -> "HIATUS"
                    item.status.equals("Finished", true) || item.status.equals("Tamat", true) -> "TAMAT"
                    else -> null
                }
                Column(Modifier.width(128.dp).clickable { onAnimeClick(item) }) {
                    Box(Modifier.fillMaxWidth().height(174.dp).clip(RoundedCornerShape(15.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                        if (item.posterUrl != null) {
                            AsyncImage(item.posterUrl, item.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        } else {
                            HomeArtwork(item, Modifier.fillMaxSize())
                        }
                        Box(
                            Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(64.dp).background(
                                Brush.verticalGradient(listOf(MaterialTheme.colorScheme.scrim.copy(alpha = 0f), MaterialTheme.colorScheme.scrim.copy(alpha = .86f)))
                            )
                        )
                        if (badge != null) {
                            Surface(Modifier.align(Alignment.TopStart).padding(7.dp), RoundedCornerShape(7.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .92f)) {
                                Text(badge, Modifier.padding(horizontal = 6.dp, vertical = 4.dp), fontSize = 7.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                        Text(item.title, Modifier.align(Alignment.BottomStart).padding(8.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary, maxLines = 2)
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(item.title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    Text("Ep ${item.latestEpisode}  •  ★ ${item.rating}", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
            }
        }
    }
}
