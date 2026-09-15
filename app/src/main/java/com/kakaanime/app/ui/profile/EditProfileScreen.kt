package com.kakaanime.app.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EditProfileScreen(
    state: EditProfileUiState,
    onBack: () -> Unit = {},
    onSave: (EditProfileUiState) -> Unit = {},
) {
    var username by remember(state.username) { mutableStateOf(state.username) }
    var bio by remember(state.bio) { mutableStateOf(state.bio) }
    var status by remember(state.status) { mutableStateOf(state.status) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                "Edit Profil",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Button(
                enabled = username.trim().isNotEmpty(),
                onClick = {
                    onSave(
                        EditProfileUiState(
                            username = username.trim(),
                            bio = bio.trim(),
                            status = status.trim().ifEmpty { "Online" },
                        )
                    )
                },
            ) { Text("Simpan") }
        }

        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(
                modifier = Modifier.align(Alignment.CenterHorizontally).size(88.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (username.isBlank()) {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                    } else {
                        Text(
                            username.take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .32f),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Informasi profil", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it.take(30) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Username") },
                        supportingText = { Text("Maks. 30 karakter") },
                        shape = RoundedCornerShape(14.dp),
                    )
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it.take(120) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 4,
                        label = { Text("Bio") },
                        supportingText = { Text("Maks. 120 karakter") },
                        shape = RoundedCornerShape(14.dp),
                    )
                    OutlinedTextField(
                        value = status,
                        onValueChange = { status = it.take(24) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Status") },
                        supportingText = { Text("Contoh: Online, Watching Anime") },
                        shape = RoundedCornerShape(14.dp),
                    )
                }
            }
        }
    }
}
