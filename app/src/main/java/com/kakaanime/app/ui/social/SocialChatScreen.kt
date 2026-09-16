package com.kakaanime.app.ui.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SocialChatScreen(
    state: SocialChatUiState,
    onBack: () -> Unit = {},
    onSendMessage: (String) -> Unit = {},
    onGroupInfoClick: () -> Unit = {},
) {
    var draft by remember { mutableStateOf("") }
    var showEmojiRow by remember { mutableStateOf(false) }
    var messages by remember(state.chatId) { mutableStateOf(state.messages) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, state.chatId) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Kembali") }
            Surface(Modifier.size(42.dp), CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                Box(contentAlignment = Alignment.Center) {
                    Text(state.title.take(2).uppercase(), fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(state.title, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, maxLines = 1)
                Text(state.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            if (state.isGroup) {
                IconButton(onClick = onGroupInfoClick) { Icon(Icons.Outlined.Info, "Info grup") }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            items(messages, key = { it.id }) { message -> ChatBubble(message) }
        }

        if (showEmojiRow) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
            ) {
                items(listOf("😀", "😂", "😍", "😭", "🔥", "❤️", "👍", "✨")) { emoji ->
                    Surface(
                        onClick = { draft += emoji },
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f),
                    ) { Text(emoji, Modifier.padding(horizontal = 8.dp, vertical = 6.dp), fontSize = 17.sp) }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().imePadding().navigationBarsPadding().padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(onClick = { showEmojiRow = !showEmojiRow }) { Icon(Icons.Outlined.EmojiEmotions, "Emoji") }
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Tulis pesan…") },
                maxLines = 4,
                shape = RoundedCornerShape(20.dp),
            )
            Spacer(Modifier.width(6.dp))
            IconButton(
                enabled = draft.isNotBlank(),
                onClick = {
                    val message = draft.trim()
                    if (message.isNotEmpty()) {
                        messages = messages + SocialChatMessageUi("local-${messages.size + 1}", "Akun Saya", message, "Baru saja", true)
                        onSendMessage(message)
                        draft = ""
                        showEmojiRow = false
                    }
                },
            ) { Icon(Icons.Outlined.Send, "Kirim") }
        }
    }
}

@Composable
private fun ChatBubble(message: SocialChatMessageUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
    ) {
        Column(horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start) {
            if (!message.isMine && message.senderName.isNotBlank()) {
                Text(message.senderName, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 8.dp))
                Spacer(Modifier.height(2.dp))
            }
            Surface(
                shape = RoundedCornerShape(
                    topStart = 18.dp, topEnd = 18.dp,
                    bottomStart = if (message.isMine) 18.dp else 5.dp,
                    bottomEnd = if (message.isMine) 5.dp else 18.dp,
                ),
                color = if (message.isMine) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .58f),
            ) {
                Column(Modifier.padding(horizontal = 13.dp, vertical = 9.dp)) {
                    Text(message.text, fontSize = 13.sp)
                    if (message.timeLabel.isNotBlank()) {
                        Spacer(Modifier.height(3.dp))
                        Text(message.timeLabel, fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
