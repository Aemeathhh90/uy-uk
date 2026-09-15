package com.kakaanime.app.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ThemeCustomizationScreen(
    state: KakaThemeState,
    onBack: () -> Unit = {},
    onAccentSelected: (KakaAccent) -> Unit = { state.accent = it },
    onModeSelected: (KakaThemeMode) -> Unit = { state.mode = it },
) {
    Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali")
            }
            Text("Tampilan", style = MaterialTheme.typography.headlineSmall)
        }
        Text(
            "Pilih warna dan mode tampilan KakaAnime.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .38f),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Warna aksen", style = MaterialTheme.typography.titleMedium)
                AccentRow(state.accent, onAccentSelected)
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .38f),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Mode tampilan", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    KakaThemeMode.entries.forEach { mode ->
                        FilterChip(
                            selected = state.mode == mode,
                            onClick = { onModeSelected(mode) },
                            label = {
                                Text(
                                    when (mode) {
                                        KakaThemeMode.LIGHT -> "Terang"
                                        KakaThemeMode.DARK -> "Gelap"
                                        KakaThemeMode.AUTO -> "Otomatis"
                                    }
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccentRow(selected: KakaAccent, onAccentSelected: (KakaAccent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KakaAccent.entries.forEach { accent ->
            Surface(
                onClick = { onAccentSelected(accent) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                shape = CircleShape,
                color = accent.primary,
                tonalElevation = if (accent == selected) 6.dp else 0.dp,
            ) {
                if (accent == selected) {
                    Text(
                        "✓",
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
    }
}
