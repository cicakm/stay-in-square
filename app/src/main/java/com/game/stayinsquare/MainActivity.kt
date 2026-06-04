package com.game.stayinsquare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.game.stayinsquare.ui.theme.StayInSquareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StayInSquareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen(modifier = Modifier.padding(innerPadding), GameViewModel())
                }
            }
        }
    }
}

@Composable
fun MyScreen(modifier: Modifier, viewModel: GameViewModel) {
    var screenWidth by remember { mutableFloatStateOf(0f) }
    var screenHeight by remember { mutableFloatStateOf(0f) }

    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        var lastFrameNanos = 0L
        while (true) {
            withFrameNanos { frameTimeNanos ->
                val deltaMS =
                    if (lastFrameNanos == 0L) 0L else (frameTimeNanos - lastFrameNanos) / 1_000_000L
                lastFrameNanos = frameTimeNanos
                viewModel.update(deltaMS, screenHeight, screenWidth)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace,
                    fontFeatureSettings = "tnum"
                ), text = "${viewModel.elapsedMS / 1000f}"
            )
        }

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .onSizeChanged {
                    screenWidth = it.width.toFloat()
                    screenHeight = it.height.toFloat()
                }
                .clickable(
                    onClick = {
                        viewModel.dot.changeDirection(6f)
                    }, indication = null,
                    interactionSource = null,
                    enabled = true,
                    onClickLabel = null,
                    role = null
                )
        ) {

            viewModel.square.let { obj ->
                drawRect(
                    color = obj.color,
                    topLeft = Offset(obj.X, obj.Y),
                    size = Size(obj.size, obj.size)
                )
            }

            viewModel.dot.let { obj ->
                drawRect(
                    color = obj.color,
                    topLeft = Offset(obj.X, obj.Y),
                    size = Size(obj.size, obj.size)
                )
            }
        }
    }
}
