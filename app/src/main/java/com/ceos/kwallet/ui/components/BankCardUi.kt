package com.ceos.kwallet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ceos.kwallet.tools.extensions.setSaturation

@Composable
fun BankCardUi(
    modifier: Modifier = Modifier,
    baseColor: Color = Color(0xFF1252C8),
    cardNumber: String = "",
) {
    // Bank Card Aspect Ratio
    val bankCardAspectRatio = 1.586f // (e.g., width:height = 85.60mm:53.98mm)
    Card(
        modifier = modifier
            .fillMaxWidth()
            // Aspect Ratio in Compose
            .aspectRatio(bankCardAspectRatio),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Box {
            BankCardBackground(baseColor = baseColor)
            Text(
                cardNumber,
                fontSize = 48.sp
            )
        }
    }
}

@Composable
fun BankCardBackground(baseColor: Color) {
    val colorSaturation75 = baseColor.setSaturation(0.75f)
    val colorSaturation50 = baseColor.setSaturation(0.5f)
    // Drawing Shapes with Canvas
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(baseColor)
    ) {
        // Drawing Circles
        drawCircle(
            color = colorSaturation75,
            center = Offset(x = size.width * 0.2f, y = size.height * 0.6f),
            radius = size.minDimension * 0.85f
        )
        drawCircle(
            color = colorSaturation50,
            center = Offset(x = size.width * 0.1f, y = size.height * 0.3f),
            radius = size.minDimension * 0.75f
        )
    }
}