package com.ceos.kwallet.ui.components

import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.zIndex
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun rememberCardCarouselState(): CardCarouselState = remember {
    CardCarouselStateImpl()
}
//https://fvilarino.medium.com/implementing-a-circular-carousel-in-jetpack-compose-cc46f2733ca7
@Composable
fun CardCarousel(modifier: Modifier = Modifier,
                 state: CardCarouselState = rememberCardCarouselState(),
                 cardRender: @Composable (index: Int) -> Unit) {
    val numberOfCards = 4
    val itemFraction = 0.75 //We are calculating how tall an item can be, based on the overall height of the composable and the fraction we receive as argument.
    val cardAspectRatio = 1.586f //width:height ratio of the card
    Layout(
        modifier = modifier
            .drag(state),
        content = {
            val angleStep = 360f / numberOfCards.toFloat()
            repeat(numberOfCards) { index ->
                val itemAngle = (state.angle + angleStep * index.toFloat()).normalizeAngle()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(if (itemAngle <= 180f) 180f - itemAngle else itemAngle - 180f)
                        .graphicsLayer {
                            cameraDistance = 12f * density
                            alpha = if (itemAngle < 90f || itemAngle > 270f) 1f else .6f
                            val scale = 1f - .2f * when {
                                itemAngle <= 180f -> itemAngle / 180f
                                else -> (360f - itemAngle) / 180f
                            }
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    cardRender(index)
                }
            }
        }
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth*itemFraction
        val itemHeight = itemWidth / cardAspectRatio
        val itemConstraints = Constraints.fixed(
            width = itemWidth.toInt(),//* cardAspectRatio,
            height = itemHeight.toInt(),
        )
        val placeables = measurables.map { measurable -> measurable.measure(itemConstraints) }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            val availableHorizontalSpace = constraints.maxWidth - itemWidth
            val horizontalOffset = availableHorizontalSpace / 2.0
            val verticalOffset = (constraints.maxHeight - itemHeight).toInt() / 2
            val angleStep = 2.0 * PI / numberOfCards.toDouble()
            placeables.forEachIndexed { index, placeable ->
                val itemAngle = (state.angle.toDouble().degreesToRadians() + (angleStep * index.toDouble())) % 360.0
                val offset = getCoordinates(
                    width = availableHorizontalSpace / 2.0,
                    height =  (constraints.maxHeight.toDouble() / 2.0 ) * state.minorAxisFactor,
                    angle = itemAngle,
                )
                placeable.placeRelative(
                    x = (horizontalOffset + offset.x).roundToInt(),
                    y = offset.y.roundToInt() + verticalOffset,
                )
            }
        }
    }
}

private fun Float.normalizeAngle(): Float = (this % 360f).let { angle -> if (this < 0f) 360f + angle else angle }
private fun Double.degreesToRadians(): Double = this / 360.0 * 2.0 * PI
private fun getCoordinates(width: Double, height: Double, angle: Double): Offset {
    val x = width * sin(angle)
    val y = height * cos(angle)
    return Offset(
        x = x.toFloat(),
        y = y.toFloat(),
    )
}

private fun Modifier.drag(
    state: CardCarouselState,
) = pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)
    coroutineScope {
        while (true) {
            val pointerInput = awaitPointerEventScope { awaitFirstDown() }
            state.stop()
            val tracker = VelocityTracker()
            val degreesPerPixel = 180f / size.width.toFloat()
            awaitPointerEventScope {
                horizontalDrag(pointerInput.id) { change ->
                    val horizontalDragOffset = state.angle + change.positionChange().x * degreesPerPixel
                    launch {
                        state.snapTo(horizontalDragOffset)
                    }
                    tracker.addPosition(change.uptimeMillis, change.position)
                    if (change.positionChange() != Offset.Zero) change.consume()
                }
                val velocity = tracker.calculateVelocity().x
                val targetAngle = decay.calculateTargetValue(
                    state.angle,
                    velocity * degreesPerPixel,
                )
                launch {
                    state.decayTo(
                        angle = targetAngle,
                        velocity = velocity * degreesPerPixel,
                    )
                }
            }
        }
    }
}