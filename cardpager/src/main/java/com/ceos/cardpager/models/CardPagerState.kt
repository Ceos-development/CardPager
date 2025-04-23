package com.ceos.cardpager.models

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow

@Stable
interface CardPagerState {
    val count: Int
    val carouselSize: Int
    val angle: Float
    val dragOffset: Float
    val stepAngle: Float
        get() = 360F / carouselSize.toFloat()
    val currentPageOffsetFractionFlow: Flow<Float>

    suspend fun stop()
    suspend fun dragging(offset: Float)
    suspend fun draggingStop(targetDrag: Float, velocity: Float)
    fun setSwipeableZoneWidth(width: Float)
}