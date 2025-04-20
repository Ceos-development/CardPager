package com.ceos.kwallet.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.abs
import kotlin.properties.Delegates

@Composable
fun rememberCardCarouselState(
    count: Int,
    carouselSize: Int = 4,
    onCurrentIndexChange: (currentIndex: Int) -> Unit = {},
): CardCarouselState = remember {
    CardCarouselStateImpl(
        initialIndex = 0,
        count = count,
        carouselSize = carouselSize,
        onCurrentIndexChange = onCurrentIndexChange,
    )
}

internal class CardCarouselStateImpl(
    val initialIndex: Int,
    override val count: Int,
    override val carouselSize: Int,
    onCurrentIndexChange: (currentIndex: Int) -> Unit,
) : CardCarouselState {
    private var _currentIndex: Int by Delegates.observable(initialIndex) { _, _, new ->
        onCurrentIndexChange.invoke(new)
    }
    private var _maxWidth by Delegates.notNull<Float>()
    private val _angle = Animatable(0f)
    private val _dragOffset = mutableFloatStateOf(0f)
    private val _angleStepTrigger = stepAngle * 0.5F
    override val currentIndex: Int
        get() = _currentIndex

    override val angle: Float
        get() = _angle.value

    override val dragOffset: Float
        get() = _dragOffset.floatValue

    init {
        onCurrentIndexChange.invoke(initialIndex)
    }

    override suspend fun stop() {
        _angle.stop()
    }

    private fun getDegreeByPixel() = 180f / _maxWidth

    override suspend fun dragging(offset: Float) {
        _dragOffset.floatValue += offset

        val maxBound = (currentIndex + 1) * stepAngle
        val minBound = -1 * maxBound
        val draggedDegrees = (dragOffset * getDegreeByPixel()).coerceIn(minBound, maxBound)
        _angle.snapTo(draggedDegrees + (currentIndex * -stepAngle))
    }

    override suspend fun draggingStop(targetDrag: Float, velocity: Float) {
        val draggedDegrees = _dragOffset.floatValue * getDegreeByPixel()

        if (draggedDegrees <= -_angleStepTrigger) {
            _currentIndex = currentIndex.plus(1).coerceAtMost(count - 1)
        } else if (draggedDegrees >= _angleStepTrigger) {
            _currentIndex = currentIndex.minus(1).coerceAtLeast(0)
        }

        animateToIndex(currentIndex, velocity)
        _dragOffset.floatValue = 0f
    }

    private suspend fun animateToIndex(index: Int, velocity: Float) {
        _angle.animateTo(
            targetValue = (index * -stepAngle),
            initialVelocity = velocity,
            animationSpec = tween()
        )
    }

    override fun setSwipeableZoneWidth(width: Float) {
        _maxWidth = width
    }

    override val currentPageOffsetFractionFlow: Flow<Float>
        get() = snapshotFlow {
            this.dragOffset
        }.map {
            val draggedDegree = abs(it) * getDegreeByPixel()
            (draggedDegree / stepAngle).coerceIn(0.0F, 1.0F)
        }
}