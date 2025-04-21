package com.ceos.kwallet.ui.components

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.abs

@Stable
interface CardCarouselState {
    val count: Int
    val carouselSize: Int
    val angle: Float
    val dragOffset: Float
    val stepAngle: Float
        get() = 360F / carouselSize.toFloat()

    suspend fun stop()
    suspend fun dragging(offset: Float)
    suspend fun draggingStop(targetDrag: Float, velocity: Float)
    fun setSwipeableZoneWidth(width: Float)

    val currentPageOffsetFractionFlow: Flow<Float>
}

