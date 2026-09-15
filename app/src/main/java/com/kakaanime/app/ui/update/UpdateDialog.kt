package com.kakaanime.app.ui.update

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kakaanime.app.ui.motion.KakaMotion

@Composable
fun UpdateDialog(
    state: UpdateUiState,
    onLater: () -> Unit = {},
    onUpdate: () -> Unit = {},
) {
    if (!state.isUpdateAvailable) return

    Dialog(
        onDismissRequest = onLater,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        AnimatedVisibility(
            visible = true,
            enter = KakaMotion.modalEnterTransition,
            exit = KakaMotion.modalExitTransition,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(.9f),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            modifier = Modifier.size(58.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primary,
                        ) {
                            Icon(
                                Icons.Outlined.Download,
                                contentDescription = "Update",
                                modifier = Modifier.padding(15.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = onLater) {
                            Icon(Icons.Outlined.Close, contentDescription = "Tutup")
                        }
                    }

                    Text(
                        "New Update Available",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Text(
                        "Versi baru KakaAnime sudah tersedia. Update untuk mendapatkan fitur dan perbaikan terbaru.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp,
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .45f),
                    ) {
                        Text(
                            "Version ${state.versionLabel}",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    if (state.changelog.isNotEmpty()) {
                        Text("Perubahan", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            state.changelog.take(5).forEach { change ->
                                Text("• $change", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(Modifier.size(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        OutlinedButton(
                            onClick = onLater,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp),
                        ) { Text("Nanti") }
                        Button(
                            onClick = onUpdate,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp),
                        ) { Text("Update") }
                    }
                }
            }
        }
    }
}
