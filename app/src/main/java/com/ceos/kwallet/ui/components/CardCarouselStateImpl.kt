package com.ceos.kwallet.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableFloatStateOf
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class CardCarouselStateImpl(eccentricity: Float = 0F, override val count: Int) :
    CardCarouselState {
    private var currentIndex = 0
    private var maxWidth by Delegates.notNull<Float>()
    private val _angle = Animatable(0f)
    private val _eccentricity = mutableFloatStateOf(eccentricity)
    private val _dragOffset = mutableFloatStateOf(0f)

    override val angle: Float
        get() = _angle.value

    override val dragOffset: Float
        get() = _dragOffset.floatValue

    override val minorAxisFactor: Float
        get() = _eccentricity.floatValue

    private val decayAnimationSpec = FloatSpringSpec(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow,
    )

    override suspend fun stop() {
        _angle.stop()
    }

    override suspend fun snapTo(angle: Float) {
        _angle.snapTo(angle)
    }

    override suspend fun decayTo(angle: Float, velocity: Float) {
        _angle.animateTo(
            targetValue = angle,
            initialVelocity = velocity,
            animationSpec = decayAnimationSpec,
        )
    }

    override fun setMinorAxisFactor(factor: Float) {
        _eccentricity.floatValue = factor.coerceIn(-1f, 1f)
    }

    override suspend fun dragging(offset: Float) {
        _dragOffset.floatValue += offset

        val degreesPerPixel = 180f / maxWidth
        val maxBound = (currentIndex + 1) * 90F
        val minBound = -1 * maxBound
        val d = (dragOffset * degreesPerPixel).coerceIn(minBound, maxBound)
        _angle.snapTo(d + (currentIndex * -90F))
    }

    override suspend fun draggingStop(targetDrag: Float, velocity: Float) {
        val degreesPerPixel = 180f / maxWidth
        val draggedDegrees = _dragOffset.floatValue * degreesPerPixel

        if (draggedDegrees <= -45) {
            currentIndex = currentIndex.plus(1).coerceAtMost(count)
        } else if (draggedDegrees >= 45) {
            currentIndex = currentIndex.minus(1).coerceAtLeast(0)
        }

        animateToIndex(currentIndex, velocity)
        _dragOffset.floatValue = 0f
    }

    private suspend fun animateToIndex(index: Int, velocity: Float) {
        _angle.animateTo(
            targetValue = (index * -90F),
            initialVelocity = velocity,
            animationSpec = tween()
        )
    }

    override fun setSwipeableZoneWidth(width: Float) {
        maxWidth = width
    }
}