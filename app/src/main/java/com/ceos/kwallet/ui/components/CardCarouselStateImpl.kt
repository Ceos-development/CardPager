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
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

@Composable
fun rememberCardCarouselState(
    count: Int,
    carouselSize: Int = 4,
    onCardIndexChange: (cardIndex: Int) -> Unit = {},
): CardCarouselState = remember {
    CardCarouselStateImpl(
        initialIndex = 0,
        count = count,
        carouselSize = carouselSize,
        onCardIndexChange = onCardIndexChange,
    )
}

internal class CardCarouselStateImpl(
    val initialIndex: Int,
    override val count: Int,
    override val carouselSize: Int,
    onCardIndexChange: (cardIndex: Int) -> Unit,
) : CardCarouselState {
    private var _currentIndex: Int = initialIndex
    private var _cardIndex: Int by Delegates.observable(initialIndex) { _, _, new ->
        onCardIndexChange.invoke(new)
    }
    private var _maxWidth by Delegates.notNull<Float>()
    private val _angle = Animatable(0f)
    private val _dragOffset = mutableFloatStateOf(0f)
    private val _angleStepTrigger = stepAngle * 0.5F

    override val angle: Float
        get() = _angle.value

    override val dragOffset: Float
        get() = _dragOffset.floatValue

    private var draggingIndexRange by Delegates.notNull<IntRange>()

    init {
        onCardIndexChange.invoke(initialIndex)
        setDraggingIndexRange(initialIndex)
    }

    override suspend fun stop() {
        _angle.stop()
    }

    private fun getDegreeByPixel() = 180f / _maxWidth

    private fun setDraggingIndexRange(currentIndex: Int) {
        draggingIndexRange = max(currentIndex - 1, 0)..min(currentIndex + 1, count)
    }

    override suspend fun dragging(offset: Float) {
        _dragOffset.floatValue += offset

        val maxBound = (_currentIndex + 1) * stepAngle
        val minBound = -1 * maxBound
        val draggedDegrees = (dragOffset * getDegreeByPixel()).coerceIn(minBound, maxBound)
        _angle.snapTo(draggedDegrees + (_currentIndex * -stepAngle))

        if (draggedDegrees <= -_angleStepTrigger) {
            _cardIndex = _cardIndex.plus(1).coerceIn(draggingIndexRange)
        } else if (draggedDegrees >= _angleStepTrigger) {
            _cardIndex = _cardIndex.minus(1).coerceIn(draggingIndexRange)
        }
    }

    override suspend fun draggingStop(targetDrag: Float, velocity: Float) {
        val draggedDegrees = _dragOffset.floatValue * getDegreeByPixel()

        if (draggedDegrees <= -_angleStepTrigger) {
            _currentIndex = _currentIndex.plus(1).coerceAtMost(count - 1)
        } else if (draggedDegrees >= _angleStepTrigger) {
            _currentIndex = _currentIndex.minus(1).coerceAtLeast(0)
        }
        animateToIndex(_currentIndex, velocity)
        _dragOffset.floatValue = 0f
        setDraggingIndexRange(_currentIndex)
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