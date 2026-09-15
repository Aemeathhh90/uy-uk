package com.kakaanime.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

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
) {
    var query by remember(state.searchQuery) { mutableStateOf(state.searchQuery) }
    var filter by remember(state.selectedFilter) { mutableStateOf(state.selectedFilter) }
    var showFilters by remember { mutableStateOf(false) }
    val filtered = remember(state.anime, query, filter) {
        state.anime.filter { anime ->
            val matchesQuery = query.isBlank() || anime.title.contains(query, true) || anime.genre.contains(query, true)
            val matchesFilter = when (filter) {
                HomeFilter.ALL -> true
                HomeFilter.ONGOING -> anime.status.equals("Ongoing", true)
                HomeFilter.FINISHED -> anime.status.equals("Finished", true)
            }
            matchesQuery && matchesFilter
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 116.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item { HomeHeader(state.username, state.isPremium, onProfileClick, onNotificationsClick) }
        item { HomeAccountStrip(state.diamonds, state.isPremium, onDiamondClick, onPremiumClick, onWatchTogetherClick) }
        if (!state.isPremium) item { HomePremiumBanner(onPremiumClick) }
        item { HomeHero(state.anime.firstOrNull(), { state.anime.firstOrNull()?.let(onAnimeClick) }) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    leadingIcon = { Icon(Icons.Outlined.Search, "Cari") },
                    placeholder = { Text("Cari Anime Di Sini") },
                )
                Surface(
                    onClick = { showFilters = !showFilters },
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = if (showFilters) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f),
                ) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Tune, "Filter") } }
            }
        }
        if (showFilters) item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(HomeFilter.entries) { option -> FilterChip(selected = filter == option, onClick = { filter = option }, label = { Text(option.label) }) }
            }
        }
        if (query.isBlank()) {
            item { HomeGlobalChat(onWatchTogetherClick) }
            if (state.continueWatching.isNotEmpty()) item { HomeContinueSection(state.continueWatching, onContinueWatchingClick) }
            item { HomeAnimeSection("New Updates", state.anime.filter { it.isNew }.ifEmpty { state.anime.sortedByDescending { it.latestEpisode } }, onAnimeClick, "NEW") }
            item { HomeAnimeSection("Trending Now", state.anime, onAnimeClick) }
        } else item {
            if (filtered.isEmpty()) {
                Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .45f)) {
                    Text("Anime tidak ditemukan", Modifier.padding(18.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else HomeAnimeSection("Hasil Pencarian", filtered, onAnimeClick)
        }
    }
}

@Composable
private fun HomeHeader(username: String, isPremium: Boolean, onProfileClick: () -> Unit, onNotificationsClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(onClick = onProfileClick, modifier = Modifier.size(54.dp), shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
            Box(contentAlignment = Alignment.Center) { Text(username.take(2).uppercase(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(username, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Text(if (isPremium) "Premium aktif" else "Akun Free", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(onClick = onNotificationsClick, modifier = Modifier.size(50.dp), shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .58f)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Notifications, "Notifikasi") }
        }
    }
}

@Composable
private fun HomeAccountStrip(keys: Int, isPremium: Boolean, onKeyClick: () -> Unit, onPremiumClick: () -> Unit, onWatchTogetherClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        HomeMiniAction({ Icon(Icons.Outlined.Key, null, tint = MaterialTheme.colorScheme.primary) }, keys.toString(), "Key", Modifier.weight(1f), onKeyClick)
        HomeMiniAction({ Icon(Icons.Outlined.WorkspacePremium, null, tint = MaterialTheme.colorScheme.primary) }, if (isPremium) "1080p" else "Free", if (isPremium) "Premium" else "Upgrade", Modifier.weight(1f), onPremiumClick)
        HomeMiniAction({ Icon(Icons.Outlined.ChatBubbleOutline, null, tint = MaterialTheme.colorScheme.primary) }, "Watch", "Nonton bareng", Modifier.weight(1.15f), onWatchTogetherClick)
    }
}

@Composable
private fun HomeMiniAction(icon: @Composable () -> Unit, title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = modifier.height(68.dp), shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .42f)) {
        Row(Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            icon(); Spacer(Modifier.width(7.dp)); Column { Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1); Text(subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1) }
        }
    }
}

@Composable
private fun HomePremiumBanner(onClick: () -> Unit) {
    Surface(onClick = onClick, Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .16f)) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(58.dp), CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = .88f)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.WorkspacePremium, null, tint = MaterialTheme.colorScheme.onPrimary) } }
            Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text("Premium", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold); Text("1080p + Auto Skip Intro", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Icon(Icons.Outlined.ArrowForward, "Upgrade", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun HomeHero(anime: HomeAnimeUi?, onClick: () -> Unit) {
    if (anime == null) return
    Box(Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(24.dp)).clickable(onClick = onClick).background(MaterialTheme.colorScheme.surfaceVariant)) {
        if (anime.posterUrl != null) AsyncImage(anime.posterUrl, anime.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.scrim.copy(alpha = .88f)))))
        Surface(Modifier.align(Alignment.TopStart).padding(12.dp), RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.scrim.copy(alpha = .72f)) { Text("TRENDING", Modifier.padding(horizontal = 9.dp, vertical = 5.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) }
        Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) { Text(anime.title, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary, maxLines = 2); Text("Episode ${anime.latestEpisode}  •  ★ ${anime.rating}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .82f)) }
    }
}

@Composable
private fun HomeGlobalChat(onClick: () -> Unit) {
    Surface(onClick = onClick, Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .24f)) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 11.dp)) {
            Text("GLOBAL CHAT", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(7.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(34.dp), CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = .18f)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.ChatBubbleOutline, null, tint = MaterialTheme.colorScheme.primary) } }
                Spacer(Modifier.width(10.dp)); Text("Komunitas KakaAnime • Open Member", Modifier.weight(1f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1); Icon(Icons.Outlined.ArrowForward, null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun HomeSectionHeader(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) { Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f)); Text("Lihat semua  ›", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun HomeContinueSection(entries: List<ContinueWatchingUi>, onClick: (ContinueWatchingUi) -> Unit) {
    Column { HomeSectionHeader("Terakhir Ditonton"); Spacer(Modifier.height(9.dp)); LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 8.dp)) {
        items(entries, key = { "${it.anime.id}:${it.episode}" }) { entry ->
            Column(Modifier.width(132.dp).clickable { onClick(entry) }) {
                Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                    if (entry.thumbnailUrl != null) AsyncImage(entry.thumbnailUrl, entry.anime.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    Surface(Modifier.align(Alignment.TopStart).padding(8.dp), RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.scrim.copy(alpha = .72f)) { Text("EP ${entry.episode}", Modifier.padding(horizontal = 7.dp, vertical = 4.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) }
                }
                Spacer(Modifier.height(6.dp)); Text(entry.anime.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1); Text(entry.episodeTitle ?: "Episode ${entry.episode}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    } }
}

@Composable
private fun HomeAnimeSection(title: String, anime: List<HomeAnimeUi>, onAnimeClick: (HomeAnimeUi) -> Unit, badge: String? = null) {
    if (anime.isEmpty()) return
    Column { HomeSectionHeader(title); Spacer(Modifier.height(9.dp)); LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(end = 8.dp)) {
        items(anime, key = { it.id }) { item ->
            Column(Modifier.width(112.dp).clickable { onAnimeClick(item) }) {
                Box(Modifier.fillMaxWidth().height(154.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                    if (item.posterUrl != null) AsyncImage(item.posterUrl, item.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    else { Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)))); Text("POSTER", Modifier.align(Alignment.Center), fontSize = 8.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                    Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(54.dp).background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.scrim.copy(alpha = .86f)))))
                    if (badge != null) Surface(Modifier.align(Alignment.TopStart).padding(7.dp), RoundedCornerShape(7.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .92f)) { Text(badge, Modifier.padding(horizontal = 6.dp, vertical = 4.dp), fontSize = 7.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) }
                }
                Spacer(Modifier.height(5.dp)); Text(item.title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1); Text("Ep ${item.latestEpisode} • ★ ${item.rating}", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    } }
}
