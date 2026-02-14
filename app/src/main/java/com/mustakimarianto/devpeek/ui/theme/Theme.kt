package com.mustakimarianto.devpeek.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ─── Color Schemes ────────────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = Purple40,
    onPrimary        = Neutral99,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,

    secondary        = PurpleDark40,
    onSecondary      = Neutral99,
    secondaryContainer = Purple90,
    onSecondaryContainer = Purple10,

    tertiary         = TertiaryLight,
    onTertiary       = Neutral99,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    background       = SurfaceLight,
    onBackground     = Neutral10,
    surface          = SurfaceLight,
    onSurface        = Neutral10,
    surfaceVariant   = Neutral95,
    onSurfaceVariant = Neutral20,

    outline          = NavInactiveLight,
)

private val DarkColorScheme = darkColorScheme(
    primary          = Purple80,
    onPrimary        = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,

    secondary        = PurpleDark80,
    onSecondary      = PurpleDark10,
    secondaryContainer = Purple20,
    onSecondaryContainer = Purple90,

    tertiary         = TertiaryLight,
    onTertiary       = TertiaryDark,
    tertiaryContainer = TertiaryDark,
    onTertiaryContainer = Color(0xFFFFD8E4),

    background       = SurfaceDark,
    onBackground     = Neutral90,
    surface          = SurfaceDark,
    onSurface        = Neutral90,
    surfaceVariant   = Neutral20,
    onSurfaceVariant = Neutral90,

    outline          = NavInactiveDark,
)

// ─── Theme ────────────────────────────────────────────────────────────────────
@Composable
fun DevPeekTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}