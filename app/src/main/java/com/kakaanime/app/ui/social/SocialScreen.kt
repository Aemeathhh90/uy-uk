package com.kakaanime.app.ui.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SocialScreen(
    state: SocialUiState,
    onProfileClick: (String) -> Unit = {},
    onWatchTogetherClick: () -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 116.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column {
                Text("Social", fontSize = 25.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold)
                Text("Nonton dan ngobrol bareng komunitas.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SocialModeCard("GLOBAL", "${state.globalOnlineCount} online", Icons.Outlined.Public, Modifier.weight(1f))
                SocialModeCard("ANIME", "${state.animeRoomCount} room", Icons.Outlined.Groups, Modifier.weight(1f))
            }
        }
        item {
            Surface(
                onClick = onWatchTogetherClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(Modifier.size(48.dp), CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = .16f)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.VideoCall, null, tint = MaterialTheme.colorScheme.primary) }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("NONTON BARENG", fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold)
                        Text("Buat atau gabung room dan tonton bareng.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("›", fontSize = 25.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item { SocialConversationCarousel(messages = state.messages, groups = state.chatGroups) }
        item { Text("Active Friends", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold) }
        if (state.friends.isEmpty()) {
            item {
                Surface(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .42f)) {
                    Text("Belum ada teman yang sedang online.", Modifier.padding(18.dp), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(state.friends, key = { it.id }) { friend ->
                Row(Modifier.fillMaxWidth().clickable { onProfileClick(friend.id) }.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(Modifier.size(46.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                        Box(contentAlignment = Alignment.Center) { Text(friend.username.take(2).uppercase(), fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(friend.username, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                        Text(friend.status, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialConversationCarousel(messages: List<SocialMessageUi>, groups: List<SocialChatGroupUi>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(end = 8.dp),
    ) {
        item {
            ConversationListCard("Recently Message", Modifier.width(340.dp)) {
                if (messages.isEmpty()) ConversationEmpty("Belum ada percakapan terbaru.")
                else messages.take(5).forEachIndexed { index, message ->
                    MessageRow(message)
                    if (index < minOf(messages.size, 5) - 1) ConversationDivider()
                }
            }
        }
        item {
            ConversationListCard("Chat Group", Modifier.width(340.dp)) {
                if (groups.isEmpty()) ConversationEmpty("Belum ada grup anime.")
                else groups.take(5).forEachIndexed { index, group ->
                    GroupRow(group)
                    if (index < minOf(groups.size, 5) - 1) ConversationDivider()
                }
            }
        }
    }
}

@Composable
private fun ConversationListCard(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier, RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .32f)) {
        Column(Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                Text("Swipe →", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun ConversationDivider() = HorizontalDivider(
    modifier = Modifier.padding(start = 50.dp),
    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .45f),
)

@Composable
private fun ConversationEmpty(text: String) = Text(text, Modifier.padding(vertical = 16.dp), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

@Composable
private fun MessageRow(message: SocialMessageUi) {
    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(Modifier.size(40.dp), CircleShape, color = MaterialTheme.colorScheme.surface) {
            Box(contentAlignment = Alignment.Center) { Text(message.username.take(2).uppercase(), fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(message.username, fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            Text(message.preview, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            if (message.timeLabel.isNotBlank()) Text(message.timeLabel, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (message.unreadCount > 0) { Spacer(Modifier.height(4.dp)); Text("${message.unreadCount}", fontSize = 9.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
        }
    }
}

@Composable
private fun GroupRow(group: SocialChatGroupUi) {
    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(Modifier.size(40.dp), RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface) {
            Box(contentAlignment = Alignment.Center) { Text(group.name.take(2).uppercase(), fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(group.name, fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, maxLines = 1)
            Text("${group.memberCount} anggota • ${group.lastMessage}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            if (group.timeLabel.isNotBlank()) Text(group.timeLabel, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (group.unreadCount > 0) { Spacer(Modifier.height(4.dp)); Text("${group.unreadCount}", fontSize = 9.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
        }
    }
}

@Composable
private fun SocialModeCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Surface(modifier, RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .42f)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Column { Text(title, fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold); Text(subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}
