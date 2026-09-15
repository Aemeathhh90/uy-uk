package com.kakaanime.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemeCustomizationScreen(
    state: KakaThemeState,
    onBack: () -> Unit = {},
    onAccentSelected: (KakaAccent) -> Unit = { state.accent = it },
    onModeSelected: (KakaThemeMode) -> Unit = { state.mode = it },
) {
    var customColorEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = KakaTokens.screenPadding, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(KakaTokens.sectionGap),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali") }
            Column(Modifier.weight(1f)) {
                Text("Appearance", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Customize how KakaAnime looks and feels", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Outlined.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }

        AppearanceSection("Theme Mode", "Choose your preferred theme mode", Icons.Outlined.Contrast) {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KakaThemeMode.entries.forEach { mode ->
                    FilterChip(selected = state.mode == mode, onClick = { onModeSelected(mode) }, label = { Text(mode.label) })
                }
            }
        }

        AppearanceSection("Accent Color", "Choose a color to personalize your experience", Icons.Outlined.Palette) {
            AccentGrid(state.accent, onAccentSelected)
            Surface(onClick = { customColorEnabled = !customColorEnabled }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(KakaTokens.smallRadius), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .42f)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Custom Color", fontWeight = FontWeight.SemiBold)
                        Text("Pick any color you like", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(if (customColorEnabled) "ON" else "›", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        AppearanceSection("Smart Colors", "Let KakaAnime adapt its palette", Icons.Outlined.Wallpaper) {
            SettingSwitchRow("Material You", "Use colors from your device wallpaper", state.materialYou, { state.materialYou = it }, Icons.Outlined.Smartphone)
            SettingSwitchRow("Anime/Manga Theme", "Use colors from the current anime artwork", state.animeDynamicTheme, { state.animeDynamicTheme = it }, Icons.Outlined.Palette)
        }

        AppearanceSection("Visual Effects", "Optional effects for a more premium interface", Icons.Outlined.Preview) {
            SettingSwitchRow("Liquid Glass", "Use translucent glass effects on supported surfaces", state.liquidGlass, { state.liquidGlass = it }, Icons.Outlined.Preview)
            SettingSwitchRow("Use Device Font", "Use your system font instead of KakaAnime's default", state.deviceFont, { state.deviceFont = it }, Icons.Outlined.FormatSize)
        }

        AppearanceSection("OLED Theme", "Pure black for AMOLED displays", Icons.Outlined.Devices) {
            SettingSwitchRow("OLED / Pure Black", "Reduce illuminated pixels on supported AMOLED screens", state.oled, {
                state.oled = it
                if (it) state.mode = KakaThemeMode.DARK
            }, Icons.Outlined.Contrast)
        }

        AppearanceSection("UI Scale", "Adjust the size and density of UI elements", Icons.Outlined.FormatSize) {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KakaUiScale.entries.forEach { scale ->
                    FilterChip(selected = state.uiScale == scale, onClick = { state.uiScale = scale }, label = { Text(scale.label) })
                }
            }
        }

        AppearanceSection("Theme Variant", "Optional finishing style", Icons.Outlined.Palette) {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KakaThemeVariant.entries.forEach { variant ->
                    FilterChip(selected = state.variant == variant, onClick = { state.variant = variant }, label = { Text(variant.label) })
                }
            }
        }

        AppearanceSection("Preview", "See how your settings look in the app", Icons.Outlined.Preview) { AppearancePreview(state) }

        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(KakaTokens.cardRadius), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .34f)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Changes apply instantly", fontWeight = FontWeight.SemiBold)
                    Text("Your appearance settings are saved by the app layer", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun AppearanceSection(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(KakaTokens.cardRadius), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .30f), tonalElevation = if (LocalKakaThemeState.current.liquidGlass) 0.dp else 1.dp) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            content()
        }
    }
}

@Composable
private fun AccentGrid(selected: KakaAccent, onSelected: (KakaAccent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { KakaAccent.entries.take(6).forEach { AccentDot(it, it == selected, onSelected) } }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { KakaAccent.entries.drop(6).forEach { AccentDot(it, it == selected, onSelected) } }
    }
}

@Composable
private fun AccentDot(accent: KakaAccent, selected: Boolean, onSelected: (KakaAccent) -> Unit) {
    Surface(onClick = { onSelected(accent) }, modifier = Modifier.size(if (selected) 46.dp else 40.dp).clip(CircleShape).then(if (selected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape) else Modifier), shape = CircleShape, color = accent.primary, tonalElevation = 3.dp) {
        if (selected) Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Check, contentDescription = "Dipilih", tint = Color.White) }
    }
}

@Composable
private fun SettingSwitchRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun AppearancePreview(state: KakaThemeState) {
    val accent = state.accent.primary
    val glassAlpha = if (state.liquidGlass) .46f else .20f
    val scale = when (state.uiScale) { KakaUiScale.COMPACT -> .92f; KakaUiScale.DEFAULT -> 1f; KakaUiScale.LARGE -> 1.08f }
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.background, border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = .35f))) {
        Column(Modifier.padding((16 * scale).dp), verticalArrangement = Arrangement.spacedBy((10 * scale).dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("KakaAnime", fontSize = (20 * scale).sp, fontWeight = FontWeight.Bold)
                    Text("Watch Anime, Your Way", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(Modifier.size((38 * scale).dp), CircleShape, color = accent.copy(alpha = .18f)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Preview, contentDescription = null, tint = accent) } }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Home", "Anime", "Library", "Profile").forEachIndexed { index, label ->
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), color = if (index == 0) accent.copy(alpha = .20f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = glassAlpha)) { Text(label, Modifier.padding(vertical = 9.dp), fontSize = (10 * scale).sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center) }
                }
            }
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = if (state.animeDynamicTheme) accent.copy(alpha = .16f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = glassAlpha)) {
                Column(Modifier.padding(14.dp)) {
                    Text("Continue Watching", fontSize = (11 * scale).sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(3.dp))
                    Text("Solo Leveling", fontSize = (16 * scale).sp, fontWeight = FontWeight.Bold)
                    Text("Episode 8  •  14m left", fontSize = (10 * scale).sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(99.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) { Box(Modifier.fillMaxWidth(.58f).height(4.dp).clip(RoundedCornerShape(99.dp)).background(accent)) }
                }
            }
        }
    }
}
