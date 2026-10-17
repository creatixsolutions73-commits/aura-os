package com.aura.os.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AuraOrb(listening: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val t = rememberInfiniteTransition(label = "orb")
    val pulse by t.animateFloat(
        initialValue = 0.92f, targetValue = if (listening) 1.12f else 1.03f,
        animationSpec = infiniteRepeatable(tween(if (listening) 700 else 2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    Canvas(modifier.size(240.dp).clip(CircleShape).clickable(onClick = onClick)) {
        val c = Offset(size.width / 2, size.height / 2)
        val r = size.minDimension / 2 * 0.6f * pulse
        drawCircle(Brush.radialGradient(listOf(Cyan.copy(alpha = 0.9f), Violet.copy(alpha = 0.7f), Color.Transparent), c, r * 1.6f), r * 1.6f, c)
        drawCircle(Brush.radialGradient(listOf(Color.White, Cyan, Violet), c, r), r, c)
        drawCircle(Cyan.copy(alpha = 0.5f), r * 1.25f, c, style = Stroke(2f))
    }
}
