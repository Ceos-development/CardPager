package com.ceos.kwallet.ui.components

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset

@Stable
interface CardCarouselState {
    val angle: Float
    val dragOffset: Float
    val minorAxisFactor: Float
    suspend fun stop()
    suspend fun snapTo(angle: Float)
    suspend fun decayTo(angle: Float, velocity: Float)
    suspend fun dragging(offset: Float)
    suspend fun draggingStop(targetDrag: Float, velocity: Float)
    fun setMinorAxisFactor(factor: Float)
    fun setSwipeableZoneWidth(width: Float)
}