package com.ceos.kwallet.ui.components

import androidx.compose.runtime.Stable

@Stable
interface CardCarouselState {
    val angle: Float
    val minorAxisFactor: Float
    suspend fun stop()
    suspend fun snapTo(angle: Float)
    suspend fun decayTo(angle: Float, velocity: Float)
    fun setMinorAxisFactor(factor: Float)
}