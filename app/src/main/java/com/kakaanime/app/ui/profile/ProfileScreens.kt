package com.kakaanime.app.ui.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MyProfileScreen(
    state: ProfileUiState,
    onEditProfile: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
    onAnimeClick: (String) -> Unit = {},
) = ProfileContent(state, onEditProfile, onAppearanceClick, {}, onAnimeClick)

@Composable
fun OtherUserProfileScreen(
    state: ProfileUiState,
    onBack: () -> Unit = {},
    onFollowToggle: () -> Unit = {},
    onAnimeClick: (String) -> Unit = {},
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
            Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        ProfileContent(Modifier.weight(1f), state, {}, {}, onFollowToggle, onAnimeClick)
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onEditProfile: () -> Unit,
    onAppearanceClick: () -> Unit,
    onFollowToggle: () -> Unit,
    onAnimeClick: (String) -> Unit,
) = ProfileContent(Modifier.fillMaxSize(), state, onEditProfile, onAppearanceClick, onFollowToggle, onAnimeClick)

@Composable
private fun ProfileContent(
    modifier: Modifier,
    state: ProfileUiState,
    onEditProfile: () -> Unit,
    onAppearanceClick: () -> Unit,
    onFollowToggle: () -> Unit,
    onAnimeClick: (String) -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    val displayName = state.nickname.ifBlank { state.username }
    var usernameCopied by remember(state.username) { mutableStateOf(false) }

    LaunchedEffect(usernameCopied) {
        if (usernameCopied) {
            delay(1400)
            usernameCopied = false
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 116.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .38f)) {
                Column(Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ProfileAvatarMotion(displayName)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(displayName, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            Text("@${state.username}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(state.status, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        if (state.isSelf) {
                            IconButton(onClick = onEditProfile) { Icon(Icons.Outlined.Edit, "Edit profile") }
                        } else {
                            Button(onClick = onFollowToggle) { Text(if (state.isFollowing) "Following" else "Follow") }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Surface(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(state.username))
                            usernameCopied = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = .42f),
                    ) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Username", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("@${state.username}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Icon(Icons.Outlined.ContentCopy, contentDescription = if (usernameCopied) "Username tersalin" else "Salin username", tint = MaterialTheme.colorScheme.primary)
                            if (usernameCopied) {
                                Spacer(Modifier.width(6.dp))
                                Text("Tersalin", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    if (state.bio.isNotBlank()) {
                        Spacer(Modifier.height(12.dp))
                        Text(state.bio, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    val equippedBadges = state.badges.filter { it.id in state.equippedBadgeIds }.take(state.maxEquippedBadges)
                    if (equippedBadges.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            equippedBadges.forEach { badge ->
                                AssistChip(onClick = {}, label = { Text(badge.name, fontSize = 9.sp) })
                            }
                        }
                    }
                }
            }
        }
        if (state.isSelf) {
            item {
                Surface(onClick = onAppearanceClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f)) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Palette, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Tampilan", fontWeight = FontWeight.SemiBold)
                            Text("Warna aksen dan mode tampilan", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("›", fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        item { SupporterCard(state) }
        if (state.badges.isNotEmpty()) item { BadgeCollectionCard(state) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileStat("Watched", state.watchedCount, Modifier.weight(1f))
                ProfileStat("Favorite", state.favoriteCount, Modifier.weight(1f))
                ProfileStat("Following", state.followingCount, Modifier.weight(1f))
            }
        }
        if (state.watchingTitles.isNotEmpty()) {
            item { Text("Watching Activity", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold) }
            items(state.watchingTitles) { title -> ListRow(title, "Sedang ditonton", Icons.Outlined.PlayCircle) { onAnimeClick(title) } }
        }
        item { Text("Favorite Anime", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold) }
        if (state.favorites.isEmpty()) item { Text("Belum ada anime favorit.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        else items(state.favorites, key = { it.id }) { anime -> ListRow(anime.title, "Episode ${anime.latestEpisode} • ★ ${anime.rating}", Icons.Outlined.Favorite) { onAnimeClick(anime.id) } }
    }
}

@Composable
private fun BadgeCollectionCard(state: ProfileUiState) {
    val unlocked = state.badges.count { it.isUnlocked }
    val equipped = state.badges.count { it.id in state.equippedBadgeIds }.coerceAtMost(state.maxEquippedBadges)
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Badge Collection", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text("$unlocked/${state.badges.size} unlocked • $equipped/${state.maxEquippedBadges} dipasang", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("Koleksi", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                state.badges.forEach { badge ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.size(34.dp), CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = if (badge.isUnlocked) .14f else .05f)) {
                            Box(contentAlignment = Alignment.Center) { Text(if (badge.isUnlocked) "✓" else "🔒", fontSize = 13.sp) }
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(badge.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            if (badge.description.isNotBlank()) Text(badge.description, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (badge.isEquipped || badge.id in state.equippedBadgeIds) Text("Terpasang", fontSize = 9.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun SupporterCard(state: ProfileUiState) {
    val range = (state.nextSupporterLevelPoints - state.supporterLevelStartPoints).coerceAtLeast(1)
    val progress = ((state.supportPoints - state.supporterLevelStartPoints).toFloat() / range).coerceIn(0f, 1f)
    val remaining = (state.nextSupporterLevelPoints - state.supportPoints).coerceAtLeast(0)
    val isMaxLevel = state.nextSupporterLevelPoints <= state.supporterLevelStartPoints || remaining == 0
    Surface(onClick = {}, Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = .10f), tonalElevation = 1.dp) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Kaka Supporter", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                Text(state.supporterLevel, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(state.supportPoints.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.width(6.dp))
                Text("SP", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                Spacer(Modifier.weight(1f))
                Text("${state.supporterBadge} • ${state.supporterBorder}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            LinearProgressIndicator(progress = { progress }, Modifier.fillMaxWidth())
            Text(if (isMaxLevel) "Level supporter sudah maksimal untuk milestone saat ini." else "$remaining SP lagi • Reward berikutnya: ${state.nextSupporterReward}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ProfileAvatarMotion(name: String) {
    var started by remember(name) { mutableStateOf(false) }
    LaunchedEffect(name) { started = true }
    val scale by animateFloatAsState(if (started) 1f else .86f, tween(260, easing = FastOutSlowInEasing), label = "Profile avatar scale")
    val alpha by animateFloatAsState(if (started) 1f else 0f, tween(190, easing = FastOutSlowInEasing), label = "Profile avatar alpha")
    Surface(Modifier.size(72.dp).graphicsLayer { scaleX = scale; scaleY = scale; this.alpha = alpha }, CircleShape, color = MaterialTheme.colorScheme.surface) {
        Box(contentAlignment = Alignment.Center) { Text(name.take(2).uppercase(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) }
    }
}

@Composable
private fun ProfileStat(label: String, value: Int, modifier: Modifier) {
    Surface(modifier, RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .42f)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value.toString(), fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ListRow(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
