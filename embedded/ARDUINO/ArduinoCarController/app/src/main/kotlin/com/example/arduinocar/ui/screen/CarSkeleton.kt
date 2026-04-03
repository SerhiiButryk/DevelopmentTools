package com.example.arduinocar.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * Very basic 2D car skeleton drawn with Compose Canvas.
 * This version uses only stroked outlines (no filled shapes) for a pure 2D skeleton look.
 */
@Composable
fun CarSkeletonCanvas(
    modifier: Modifier = Modifier,
    outlineColor: Color = Color.DarkGray,
    wheelColor: Color = Color.Black,
    strokeWidth: Float = 6f,
    driving: Boolean = false,
    speed: Float = 0.5f // 0..1 relative speed for animation timing
) {
    // Infinite transition drives wheel rotation and road movement when `driving` is true
    val infinite = rememberInfiniteTransition()
    val animSpeed = (0.35f + speed.coerceIn(0f, 1f) * 1.5f)

    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = if (driving) infiniteRepeatable(tween(durationMillis = (2000 / animSpeed).toInt(), easing = LinearEasing), repeatMode = RepeatMode.Restart) else infiniteRepeatable(tween(durationMillis = 1000, easing = LinearEasing), repeatMode = RepeatMode.Restart)
    )

    Canvas(
        modifier = modifier
            .aspectRatio(2f)
    ) {
        drawCarSkeletonOutline(outlineColor, wheelColor, strokeWidth, driving, phase, speed)
    }
}

private fun DrawScope.drawCarSkeletonOutline(
    outlineColor: Color,
    wheelColor: Color,
    strokeWidth: Float,
    driving: Boolean,
    phase: Float,
    speed: Float
) {
    val w = size.width
    val h = size.height

    // Chassis outline (rounded rectangle)
    val bodyLeft = w * 0.12f
    val bodyRight = w * 0.88f
    val bodyTop = h * 0.38f
    // Add a small bobbing motion to the chassis when driving
    val bobAmplitude = if (driving) h * 0.01f * (0.5f + speed) else 0f
    val bob = sin(phase * PI.toFloat() * 2f) * bobAmplitude
    val bodyBottom = h * 0.62f + bob
    val bodySize = Size(bodyRight - bodyLeft, bodyBottom - bodyTop)
    drawRoundRect(
        color = outlineColor,
        topLeft = Offset(bodyLeft, bodyTop),
        size = bodySize,
        cornerRadius = CornerRadius(x = 12f, y = 12f),
        style = Stroke(width = strokeWidth)
    )

    // Roof / windshield / rear outline (polyline)
    val roofRear = Offset(w * 0.28f, bodyTop)
    val roofApex = Offset(w * 0.5f, bodyTop - h * 0.14f)
    val roofFront = Offset(w * 0.72f, bodyTop)
    drawLine(color = outlineColor, start = roofRear, end = roofApex, strokeWidth = strokeWidth, cap = StrokeCap.Round)
    drawLine(color = outlineColor, start = roofApex, end = roofFront, strokeWidth = strokeWidth, cap = StrokeCap.Round)

    // Wheel outlines (stroked circles)
    val wheelRadius = h * 0.12f
    val rearWheelCenter = Offset(w * 0.30f, bodyBottom + wheelRadius * 0.6f)
    val frontWheelCenter = Offset(w * 0.70f, bodyBottom + wheelRadius * 0.6f)
    drawCircle(color = wheelColor, radius = wheelRadius, center = rearWheelCenter, style = Stroke(width = strokeWidth))
    drawCircle(color = wheelColor, radius = wheelRadius, center = frontWheelCenter, style = Stroke(width = strokeWidth))

    // Wheel spoke (simple single spoke to illustrate rotation)
    val spokeLength = wheelRadius * 0.9f
    val rotationDeg = if (driving) phase * 360f * (1f + speed * 2f) else 0f

    // rear spoke
    rotate(rotationDeg, pivot = rearWheelCenter) {
        drawLine(
            color = wheelColor,
            start = rearWheelCenter,
            end = Offset(rearWheelCenter.x, rearWheelCenter.y - spokeLength),
            strokeWidth = strokeWidth / 1.5f,
            cap = StrokeCap.Round
        )
    }

    // front spoke
    rotate(rotationDeg, pivot = frontWheelCenter) {
        drawLine(
            color = wheelColor,
            start = frontWheelCenter,
            end = Offset(frontWheelCenter.x, frontWheelCenter.y - spokeLength),
            strokeWidth = strokeWidth / 1.5f,
            cap = StrokeCap.Round
        )
    }

    // Simple axle line connecting wheels
    drawLine(
        color = Color.LightGray,
        start = Offset(rearWheelCenter.x - wheelRadius * 0.6f, rearWheelCenter.y),
        end = Offset(frontWheelCenter.x + wheelRadius * 0.6f, frontWheelCenter.y),
        strokeWidth = strokeWidth / 2,
        cap = StrokeCap.Round
    )

    // Moving road dashed lines beneath the car to create driving illusion
    val roadTop = bodyBottom + wheelRadius * 1.4f + h * 0.04f
    val segmentWidth = w * 0.08f
    val gap = segmentWidth * 0.6f
    // offset moves with phase; multiply by speed for faster motion
    val offset = (phase * (segmentWidth + gap) * (1f + speed * 2f)) % (segmentWidth + gap)
    var x = -offset
    while (x < w) {
        drawRoundRect(
            color = Color.LightGray,
            topLeft = Offset(x, roadTop),
            size = Size(segmentWidth, h * 0.02f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        x += segmentWidth + gap
    }
}

@Preview(showBackground = true)
@Composable
private fun CarSkeletonPreview() {
    MaterialTheme {
        CarSkeletonCanvas(modifier = Modifier.size(240.dp))
    }
}
