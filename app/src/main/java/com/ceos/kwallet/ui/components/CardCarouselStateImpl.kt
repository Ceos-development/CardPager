package com.ceos.kwallet.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf

class CardCarouselStateImpl(eccentricity: Float = 0F) : CardCarouselState {
    private val _angle = Animatable(0f)
    private val _eccentricity = mutableFloatStateOf(eccentricity)

    override val angle: Float
        get() = _angle.value

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
}