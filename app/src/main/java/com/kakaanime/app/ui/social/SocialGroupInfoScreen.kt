package com.kakaanime.app.ui.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SocialGroupInfoScreen(
    state: SocialChatUiState,
    availableFriends: List<SocialFriendUi>,
    onBack: () -> Unit = {},
    onMembersChanged: (List<SocialGroupMemberUi>) -> Unit = {},
) {
    var members by remember(state.chatId) { mutableStateOf(state.members) }
    var addingMembers by remember { mutableStateOf(false) }

    if (addingMembers) {
        GroupMemberPickerScreen(
            members = members,
            availableFriends = availableFriends,
            onBack = { addingMembers = false },
            onDone = { selected ->
                members = members + selected.map { SocialGroupMemberUi(it.id, it.username, it.id == "self") }
                    .distinctBy { it.id }
                onMembersChanged(members)
                addingMembers = false
            },
        )
        return
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
            Column(Modifier.weight(1f)) {
                Text("Info Grup", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                Text(state.title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = { addingMembers = true }) {
                Icon(Icons.Outlined.PersonAdd, "Tambah anggota")
            }
        }

        Surface(
            Modifier.fillMaxWidth().padding(16.dp),
            RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .35f),
        ) {
            Column(Modifier.padding(18.dp)) {
                Text(state.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(4.dp))
                Text("${members.size} anggota • Grup anime", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Anggota", Modifier.weight(1f), fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            OutlinedButton(onClick = { addingMembers = true }) {
                Icon(Icons.Outlined.PersonAdd, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Tambah")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 10.dp, 16.dp, 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(members, key = { it.id }) { member ->
                GroupMemberRow(
                    member = member,
                    onRemove = if (member.isSelf) null else {
                        {
                            members = members.filterNot { it.id == member.id }
                            onMembersChanged(members)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun GroupMemberPickerScreen(
    members: List<SocialGroupMemberUi>,
    availableFriends: List<SocialFriendUi>,
    onBack: () -> Unit,
    onDone: (List<SocialFriendUi>) -> Unit,
) {
    val existingIds = members.map { it.id }.toSet()
    var selectedIds by remember { mutableStateOf(emptySet<String>()) }
    val candidates = availableFriends.filter { it.id !in existingIds && it.id != "self" }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
            Column(Modifier.weight(1f)) {
                Text("Tambah Anggota", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                Text("Pilih teman yang ingin dimasukkan", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Button(
                enabled = selectedIds.isNotEmpty(),
                onClick = { onDone(candidates.filter { it.id in selectedIds }) },
            ) { Text("Selesai") }
        }

        if (candidates.isEmpty()) {
            Text(
                "Semua teman yang tersedia sudah menjadi anggota grup.",
                Modifier.padding(20.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 32.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(candidates, key = { it.id }) { friend ->
                    val selected = friend.id in selectedIds
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable {
                            selectedIds = if (selected) selectedIds - friend.id else selectedIds + friend.id
                        },
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .3f),
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(Modifier.size(44.dp), CircleShape, color = MaterialTheme.colorScheme.surface) {
                                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(friend.username.take(2).uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(friend.username, fontWeight = FontWeight.SemiBold)
                                Text(friend.status, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(if (selected) "Dipilih" else "Tambah", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupMemberRow(member: SocialGroupMemberUi, onRemove: (() -> Unit)?) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(Modifier.size(44.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(member.username.take(2).uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(member.username, fontWeight = FontWeight.SemiBold)
            Text(if (member.isSelf) "Kamu" else "Anggota", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (onRemove != null) {
            IconButton(onClick = onRemove) { Icon(Icons.Outlined.RemoveCircleOutline, "Hapus anggota") }
        }
    }
}
