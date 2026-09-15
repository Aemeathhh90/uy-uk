package com.kakaanime.app.ui.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationCenterScreen(
    state: NotificationUiState,
    onBack: () -> Unit = {},
    onNotificationClick: (NotificationUi) -> Unit = {},
    onMarkAllRead: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                "Notifikasi",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall,
            )
            if (state.unreadCount > 0) {
                Text(
                    "Tandai dibaca",
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        if (state.notifications.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .45f),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Outlined.NotificationsNone, contentDescription = null)
                    Text("Belum ada notifikasi", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Update episode anime favoritmu akan muncul di sini.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.notifications, key = { it.id }) { notification ->
                    NotificationRow(notification, onNotificationClick)
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    notification: NotificationUi,
    onNotificationClick: (NotificationUi) -> Unit,
) {
    Surface(
        onClick = { onNotificationClick(notification) },
        modifier = Modifier.fillMaxWidth(),
        color = if (notification.isUnread) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = .34f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .3f)
        },
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    notification.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    notification.timeLabel,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Text(notification.message, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
