package com.ceos.kwallet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ceos.kwallet.ui.components.BankCardUi
import com.ceos.kwallet.ui.components.CardCarousel
import com.ceos.kwallet.ui.components.rememberCardCarouselState
import com.ceos.kwallet.ui.theme.KWalletTheme
import kotlinx.coroutines.flow.map

val colors = listOf(
    Color(0xFFF44336),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF673AB7),
    Color(0xFF3F51B5),
    Color(0xFF2196F3),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KWalletTheme {
                Column {

                    var cardIndex by remember { mutableIntStateOf(0) }
                    val subtitle by remember {
                        derivedStateOf {
                            "Current card index is: $cardIndex"
                        }
                    }
                    val state = rememberCardCarouselState(11) { currentIndex ->
                        cardIndex = currentIndex
                    }
                    val alpha by state.currentPageOffsetFractionFlow.map {
                        val alphaDelta = if (it <= 0.5F) {
                            1 - (it / 0.5F)
                        } else {
                            (it - 0.5F) / 0.5F
                        }
                        alphaDelta
                    }.collectAsState(1.0F)
                    CardCarousel(
                        modifier = Modifier.weight(1F),
                        state = state
                    ) { index ->
                        BankCardUi(
                            baseColor = colors[index % colors.size],
                            cardNumber = index.toString()
                        )
                    }

                    Column(modifier = Modifier.weight(1F)) {
                        Text(
                            subtitle,
                            color = Color.Black.copy(alpha = alpha)
                        )
                    }
                }
            }
        }
    }
}