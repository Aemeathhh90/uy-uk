package com.kakaanime.app.ui.social

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakaanime.app.ui.motion.KakaMotion
import com.kakaanime.app.ui.monetization.DiamondPremiumScreen
import com.kakaanime.app.ui.monetization.MonetizationUiState
import kotlinx.coroutines.delay

@Composable
fun WatchTogetherScreen(
    state: WatchTogetherUiState,
    onBack: () -> Unit = {},
    onCreateRoom: (WatchTogetherVisibility) -> Unit = {},
    onJoinByCode: (String) -> Unit = {},
    onJoinRoom: (WatchTogetherRoomUi) -> Unit = {},
    onPremiumClick: () -> Unit = {},
) {
    var showCreate by remember { mutableStateOf(false) }
    var createVisible by remember { mutableStateOf(false) }
    var roomCode by remember { mutableStateOf("") }
    var showPremium by remember { mutableStateOf(false) }
    var premiumVisible by remember { mutableStateOf(false) }

    LaunchedEffect(showCreate) {
        if (showCreate) createVisible = true
        else if (createVisible) {
            delay(150)
            createVisible = false
        }
    }

    LaunchedEffect(showPremium) {
        if (showPremium) premiumVisible = true
        else if (premiumVisible) {
            delay(150)
            premiumVisible = false
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
                Column(Modifier.weight(1f)) {
                    Text("Watch Together", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Nonton bareng secara sinkron.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Outlined.VideoCall, null, tint = MaterialTheme.colorScheme.primary)
            }

            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    CreateRoomCard(
                        isPremium = state.isPremium,
                        onClick = { if (state.isPremium) showCreate = true else showPremium = true },
                    )
                }
                item {
                    Surface(
                        Modifier.fillMaxWidth(),
                        RoundedCornerShape(22.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .32f),
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Join Private Room", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(Modifier.height(4.dp))
                            Text("Masukkan kode room untuk bergabung.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = roomCode,
                                    onValueChange = { roomCode = it.uppercase().filter(Char::isLetterOrDigit).take(8) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("ROOM CODE") },
                                )
                                Spacer(Modifier.width(8.dp))
                                Button(enabled = roomCode.isNotBlank(), onClick = { onJoinByCode(roomCode.trim()) }) { Text("Join") }
                            }
                        }
                    }
                }
                item {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Public Rooms", Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("${state.publicRooms.size} tersedia", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (state.publicRooms.isEmpty()) {
                    item {
                        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .3f)) {
                            Text("Belum ada public room yang aktif.", Modifier.padding(18.dp), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    items(state.publicRooms, key = { it.id }) { room ->
                        RoomCard(room, onJoin = { onJoinRoom(room) })
                    }
                }
            }
        }

        if (createVisible || showCreate) {
            Box(
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = .56f))
                    .clickable { showCreate = false },
                contentAlignment = Alignment.Center,
            ) {
                AnimatedVisibility(
                    visible = createVisible && showCreate,
                    enter = KakaMotion.modalEnterTransition,
                    exit = KakaMotion.modalExitTransition,
                ) {
                    CreateWatchTogetherDialog(
                        isPremium = state.isPremium,
                        onDismiss = { showCreate = false },
                        onPremiumClick = { showCreate = false; showPremium = true },
                        onCreate = {
                            onCreateRoom(it)
                            showCreate = false
                        },
                    )
                }
            }
        }

        if (premiumVisible || showPremium) {
            Box(
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = .62f)),
            ) {
                AnimatedVisibility(
                    visible = premiumVisible && showPremium,
                    enter = KakaMotion.modalEnterTransition,
                    exit = KakaMotion.modalExitTransition,
                ) {
                    Column(Modifier.fillMaxSize()) {
                        DiamondPremiumScreen(
                            state = MonetizationUiState(diamonds = 6, isPremium = false),
                            onBack = { showPremium = false },
                            onWatchAd = {},
                            onPremiumClick = { onPremiumClick() },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateRoomCard(isPremium: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(50.dp), CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = .14f)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.VideoCall, null, tint = MaterialTheme.colorScheme.primary) }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Create Room", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                Text(if (isPremium) "Buat room Public atau Private." else "Premium diperlukan untuk membuat room.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isPremium) Text("›", fontSize = 25.sp, color = MaterialTheme.colorScheme.primary)
            else Icon(Icons.Outlined.Lock, "Premium", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun CreateWatchTogetherDialog(
    isPremium: Boolean,
    onDismiss: () -> Unit,
    onPremiumClick: () -> Unit,
    onCreate: (WatchTogetherVisibility) -> Unit,
) {
    var visibility by remember { mutableStateOf(WatchTogetherVisibility.PUBLIC) }
    Surface(
        Modifier.fillMaxWidth().padding(24.dp).clickable(enabled = true, onClick = {}),
        RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 5.dp,
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Create Watch Room", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text("Pilih siapa yang bisa menemukan room ini.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = visibility == WatchTogetherVisibility.PUBLIC,
                    onClick = { visibility = WatchTogetherVisibility.PUBLIC },
                    label = { Text("Public") },
                    leadingIcon = { Icon(Icons.Outlined.Public, null, Modifier.size(17.dp)) },
                )
                FilterChip(
                    selected = visibility == WatchTogetherVisibility.PRIVATE,
                    onClick = { visibility = WatchTogetherVisibility.PRIVATE },
                    label = { Text("Private") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, Modifier.size(17.dp)) },
                )
            }
            Text(
                if (isPremium) "Room siap dibuat. Pilih visibilitas lalu lanjutkan."
                else "Create Room hanya tersedia untuk Premium.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = onDismiss) { Text("Batal") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { if (isPremium) onCreate(visibility) else { onDismiss(); onPremiumClick() } }) { Text(if (isPremium) "Create" else "Upgrade") }
            }
        }
    }
}

@Composable
private fun RoomCard(room: WatchTogetherRoomUi, onJoin: () -> Unit) {
    Surface(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(44.dp), RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.PlayArrow, null, tint = MaterialTheme.colorScheme.primary) }
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(room.name, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                    Text("${room.animeTitle} • Episode ${room.episode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Outlined.Public, "Public", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Group, null, Modifier.size(17.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(5.dp))
                Text("${room.participants.size} orang", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                Button(onClick = onJoin) { Text("Join") }
            }
        }
    }
}

@Composable
fun WatchTogetherRoomScreen(
    room: WatchTogetherRoomUi,
    isHost: Boolean,
    onBack: () -> Unit = {},
    onLeave: () -> Unit = {},
    onCopyRoomCode: (String) -> Unit = {},
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
            Column(Modifier.weight(1f)) {
                Text(room.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                Text(if (isHost) "Host • ${room.visibility.label}" else "Member • ${room.visibility.label}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(if (room.visibility == WatchTogetherVisibility.PRIVATE) Icons.Outlined.Lock else Icons.Outlined.Public, null, tint = MaterialTheme.colorScheme.primary)
        }

        Column(
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(Modifier.fillMaxWidth().height(205.dp), RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .45f)) {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.PlayArrow, null, Modifier.size(52.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    Text("Player Watch Together", fontWeight = FontWeight.ExtraBold)
                    Text("${room.animeTitle} • Episode ${room.episode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(10.dp))
                    Text("00:42:18  ━━━━━━━━━  01:12:30", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .3f)) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Participants", Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("${room.participants.size}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.height(8.dp))
                    room.participants.forEach { participant ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(Modifier.size(36.dp), CircleShape, color = MaterialTheme.colorScheme.surface) {
                                Box(contentAlignment = Alignment.Center) { Text(participant.username.take(2).uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold) }
                            }
                            Spacer(Modifier.width(9.dp))
                            Column(Modifier.weight(1f)) {
                                Text(participant.username, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text(if (participant.isHost) "Host" else participant.status, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (participant.isHost) Icon(Icons.Outlined.VideoCall, "Host", Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (room.visibility == WatchTogetherVisibility.PRIVATE && room.roomCode.isNotBlank()) {
                    OutlinedButton(onClick = { onCopyRoomCode(room.roomCode) }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Outlined.ContentCopy, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(5.dp))
                        Text(room.roomCode)
                    }
                }
                OutlinedButton(onClick = onLeave, modifier = Modifier.weight(1f)) { Text("Leave Room") }
            }
        }
    }
}
