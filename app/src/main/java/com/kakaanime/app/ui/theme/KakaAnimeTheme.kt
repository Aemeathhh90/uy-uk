package com.kakaanime.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class KakaAccent(val primary: Color, val secondary: Color) {
    Blue(Color(0xFF2196F3), Color(0xFF64B5F6)),
    Purple(Color(0xFF9C6BFF), Color(0xFFB58CFF)),
    Pink(Color(0xFFFF5FA2), Color(0xFFFF82B7)),
    Red(Color(0xFFFF4D67), Color(0xFFFF7186)),
    Green(Color(0xFF35C98A), Color(0xFF5BE0A6)),
    Cyan(Color(0xFF20C8E8), Color(0xFF58DDF2)),
    Orange(Color(0xFFFF9A3D), Color(0xFFFFB66B))
}

enum class KakaThemeMode(val label: String) { LIGHT("Light"), DARK("Dark"), AUTO("System") }
enum class KakaUiScale(val label: String) { COMPACT("Compact"), DEFAULT("Default"), LARGE("Large") }
enum class KakaThemeVariant(val label: String) { VIBRANT("Vibrant"), MUTED("Muted"), HIGH_CONTRAST("High Contrast") }

object KakaTokens {
    val screenPadding = 18.dp
    val sectionGap = 14.dp
    val cardGap = 12.dp
    val compactGap = 8.dp
    val cardRadius = 20.dp
    val smallRadius = 14.dp
    val pillRadius = 999.dp
    val minimumTouchTarget = 48.dp
    val iconSize = 24.dp
    val smallIconSize = 20.dp
    val progressHeight = 4.dp
}

private val KakaTypography = Typography(
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
)

@Stable
class KakaThemeState(accent: KakaAccent = KakaAccent.Blue, mode: KakaThemeMode = KakaThemeMode.DARK) {
    var accent by mutableStateOf(accent)
    var mode by mutableStateOf(mode)
    var oled by mutableStateOf(false)
    var materialYou by mutableStateOf(false)
    var animeDynamicTheme by mutableStateOf(false)
    var liquidGlass by mutableStateOf(false)
    var deviceFont by mutableStateOf(false)
    var uiScale by mutableStateOf(KakaUiScale.DEFAULT)
    var variant by mutableStateOf(KakaThemeVariant.VIBRANT)
    constructor(accent: KakaAccent, darkMode: Boolean) : this(accent, if (darkMode) KakaThemeMode.DARK else KakaThemeMode.LIGHT)
    var darkMode: Boolean
        get() = mode == KakaThemeMode.DARK
        set(value) { mode = if (value) KakaThemeMode.DARK else KakaThemeMode.LIGHT }
}

val LocalKakaThemeState = compositionLocalOf { KakaThemeState() }

@Composable
fun rememberKakaThemeState(): KakaThemeState = remember { KakaThemeState() }

@Composable
fun KakaAnimeTheme(themeState: KakaThemeState = rememberKakaThemeState(), content: @Composable () -> Unit) {
    val isDark = when (themeState.mode) {
        KakaThemeMode.DARK -> true
        KakaThemeMode.LIGHT -> false
        KakaThemeMode.AUTO -> isSystemInDarkTheme()
    }
    val useOled = themeState.oled && isDark
    val background = if (useOled) Color.Black else if (isDark) Color(0xFF090D12) else Color(0xFFF5F8FC)
    val surface = if (useOled) Color.Black else if (isDark) Color(0xFF111820) else Color.White
    val surfaceVariant = if (useOled) Color(0xFF101010) else if (isDark) Color(0xFF17212B) else Color(0xFFE9F0F7)

    val primary = themeState.accent.primary
    val secondary = themeState.accent.secondary
    val onPrimary = if (primary.luminance() > 0.55f) Color.Black else Color.White
    val onSecondary = if (secondary.luminance() > 0.55f) Color.Black else Color.White
    val primaryContainer = primary.copy(alpha = if (isDark) .22f else .14f)
    val secondaryContainer = secondary.copy(alpha = if (isDark) .20f else .14f)

    val colors = if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = primary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = secondary,
            tertiary = secondary,
            onTertiary = onSecondary,
            background = background,
            surface = surface,
            surfaceVariant = surfaceVariant,
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = primary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = secondary,
            tertiary = secondary,
            onTertiary = onSecondary,
            background = background,
            surface = surface,
            surfaceVariant = surfaceVariant,
        )
    }

    CompositionLocalProvider(LocalKakaThemeState provides themeState) {
        MaterialTheme(colorScheme = colors, typography = if (themeState.deviceFont) Typography() else KakaTypography, content = content)
    }
}
