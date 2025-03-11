package io.fromto.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerPlaceholderForText(
    fromText: String,
    lineHeight: Dp = 16.dp,
    lineSpacing: Dp = 4.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    var lineWidths by remember { mutableStateOf(emptyList<Float>()) }

    // Invisible Text composable to measure the layout.
    Text(
        text = fromText,
        style = textStyle,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Visible,
        color = Color.Transparent,
        onTextLayout = { layoutResult ->
            val widths = (0 until layoutResult.lineCount).map { index ->
                layoutResult.getLineRight(index) - layoutResult.getLineLeft(index)
            }
            lineWidths = widths
        }
    )

    val density = LocalDensity.current
    Column(
        verticalArrangement = Arrangement.spacedBy(lineSpacing),
    ) {
        lineWidths.forEach { widthPx ->
            val widthDp = with(density) { widthPx.toDp() }
            ShimmerTextLine(modifier = Modifier.width(widthDp), lineHeight = lineHeight)
        }
    }
}

@Composable
private fun ShimmerTextLine(
    modifier: Modifier,
    lineHeight: Dp
) {
    val transition = rememberInfiniteTransition()
    val shimmerTranslateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        )
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.4f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerTranslateAnim - 200f, 0f),
        end = Offset(shimmerTranslateAnim, 0f)
    )

    Spacer(
        modifier = modifier
            .height(lineHeight)
            .background(
                brush, shape = MaterialTheme.shapes.medium
            )
    )
}
