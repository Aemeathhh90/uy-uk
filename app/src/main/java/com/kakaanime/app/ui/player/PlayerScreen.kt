package com.kakaanime.app.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.HighQuality
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerScreen(
    state: PlayerUiState,
    onBack: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onPreviousEpisode: () -> Unit = {},
    onNextEpisode: () -> Unit = {},
    onQualitySelected: (PlayerQuality) -> Unit = {},
    onSkipIntro: () -> Unit = {},
    onSkipOutro: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) { Text("‹ Kembali") }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(state.animeTitle, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(
                    "Episode ${state.episode}${if (state.episodeTitle.isNotBlank()) " • ${state.episodeTitle}" else ""}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }

        Surface(
            Modifier.fillMaxWidth().weight(1f),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (state.playbackState) {
                    PlayerPlaybackState.LOADING -> {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(10.dp))
                        Text("Menyiapkan video…", fontWeight = FontWeight.Bold)
                        Text("Tunggu sebentar.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    PlayerPlaybackState.BUFFERING -> {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(10.dp))
                        Text("Buffering…", fontWeight = FontWeight.Bold)
                        Text("Video sedang dilanjutkan.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    PlayerPlaybackState.ERROR -> {
                        Icon(Icons.Outlined.Refresh, null, modifier = Modifier.width(42.dp).height(42.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Video gagal diputar", fontWeight = FontWeight.Bold)
                        Text(
                            state.errorMessage ?: "Coba lagi atau pilih episode lain.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = onRetry) {
                            Icon(Icons.Outlined.Refresh, null)
                            Spacer(Modifier.width(6.dp))
                            Text("Coba Lagi")
                        }
                    }
                    PlayerPlaybackState.COMPLETED -> {
                        Icon(Icons.Filled.CheckCircle, null, modifier = Modifier.width(42.dp).height(42.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Episode selesai", fontWeight = FontWeight.Bold)
                        if (state.hasNextEpisode) {
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onNextEpisode) { Text("Episode Berikutnya") }
                        } else {
                            Text("Tidak ada episode berikutnya.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    PlayerPlaybackState.READY -> {
                        Icon(Icons.Outlined.HighQuality, null, modifier = Modifier.width(48.dp).height(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("KakaAnime Player", fontWeight = FontWeight.Bold)
                        Text("Playback siap.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        if (state.showSkipIntro && state.canSkipIntro) {
            Button(onClick = onSkipIntro, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.FastForward, null)
                Spacer(Modifier.width(8.dp))
                Text("Skip Intro")
            }
        }

        if (state.showSkipOutro && state.canSkipOutro) {
            Button(onClick = onSkipOutro, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.FastForward, null)
                Spacer(Modifier.width(8.dp))
                Text("Skip Outro")
            }
        }

        LinearProgressIndicator(
            progress = { state.progressPercent.coerceIn(0, 100) / 100f },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onPreviousEpisode, enabled = state.hasPreviousEpisode) {
                Icon(Icons.Filled.SkipPrevious, "Episode sebelumnya")
            }
            IconButton(
                onClick = onPlayPause,
                enabled = state.playbackState == PlayerPlaybackState.READY || state.playbackState == PlayerPlaybackState.BUFFERING,
            ) {
                Icon(if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, "Putar")
            }
            IconButton(onClick = onNextEpisode, enabled = state.hasNextEpisode) {
                Icon(Icons.Filled.SkipNext, "Episode berikutnya")
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Kualitas", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(8.dp))
            PlayerQuality.values().forEach { quality ->
                FilterChip(
                    selected = state.quality == quality,
                    onClick = { if (quality != PlayerQuality.P1080 || state.isPremium) onQualitySelected(quality) },
                    label = { Text(quality.label) },
                    enabled = quality != PlayerQuality.P1080 || state.isPremium,
                    modifier = Modifier.padding(horizontal = 3.dp),
                )
            }
        }
        if (!state.isPremium) {
            Text("1080p tersedia untuk Premium.", Modifier.fillMaxWidth(), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
