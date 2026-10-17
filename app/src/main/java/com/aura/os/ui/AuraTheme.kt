package com.aura.os.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Violet = Color(0xFF7C4DFF)
val Cyan = Color(0xFF00E5FF)

@Composable
fun AuraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Violet, secondary = Cyan,
            background = Color.Black, surface = Color.Black, onBackground = Color.White, onSurface = Color.White
        ),
        content = content
    )
}
