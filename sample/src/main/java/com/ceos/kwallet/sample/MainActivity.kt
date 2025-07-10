package com.ceos.kwallet.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ceos.cardpager.components.CardPager
import com.ceos.cardpager.models.rememberCardPagerState
import com.ceos.kwallet.sample.ui.components.BankCardUi
import com.ceos.kwallet.sample.ui.theme.KWalletTheme
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(modifier = Modifier.padding(innerPadding)) {

                        var cardIndex by remember { mutableIntStateOf(0) }
                        val subtitle by remember {
                            derivedStateOf {
                                "Current card index is: $cardIndex"
                            }
                        }
                        val state = rememberCardPagerState(11, initialIndex = 3){
                            cardIndex = it
                        }
                        CardPager(
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
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}