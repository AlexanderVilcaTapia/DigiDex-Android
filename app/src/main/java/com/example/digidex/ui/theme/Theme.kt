package com.example.digidex.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DigiColorScheme = lightColorScheme(
    primary            = DigiBlue,
    onPrimary          = DigiOnPrimary,
    primaryContainer   = DigiBlueLight,
    secondary          = DigiAccent,
    background         = DigiBackground,
    surface            = DigiSurface,
    onBackground       = DigiNavy,
    onSurface          = DigiNavy
)

@Composable
fun DigiDexTheme(content: @Composable () -> Unit) {
    val colorScheme = DigiColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DigiNavy.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}