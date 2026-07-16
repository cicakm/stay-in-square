package com.game.stayinsquare

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen() {
    val viewModel = GameViewModel()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                        fontFeatureSettings = "tnum"
                    ), text = "${viewModel.elapsedMS / 1000f}"
                )
            }, actions = {
                IconButton(onClick = { viewModel.isRunning = !viewModel.isRunning }) {
                    if (viewModel.isRunning) {
                        Icon(
                            painter = painterResource(R.drawable.icons_pause),
                            contentDescription = "Icon pause",
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icons_start),
                            contentDescription = "Icon start",
                        )
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }) { innerPadding ->
        MyScreen(
            modifier = Modifier.padding(innerPadding), viewModel
        )
    }
}

@Composable
fun MyScreen(
    modifier: Modifier, viewModel: GameViewModel
) {
    var screenWidth by remember { mutableFloatStateOf(0f) }
    var screenHeight by remember { mutableFloatStateOf(0f) }
    val squareColor = MaterialTheme.colorScheme.primary
    val dotColor = MaterialTheme.colorScheme.onPrimary

    LaunchedEffect(viewModel.isRunning) {
        if (!viewModel.isRunning) return@LaunchedEffect
        var lastFrameNanos = 0L
        while (!viewModel.gameOver) {
            withFrameNanos { frameTimeNanos ->
                val deltaMS =
                    if (lastFrameNanos == 0L) 0L else (frameTimeNanos - lastFrameNanos) / 1_000_000L
                lastFrameNanos = frameTimeNanos
                viewModel.update(deltaMS, screenWidth, screenHeight)
            }
        }
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
                    viewModel.isRunning = true
                },
                indication = null,
                interactionSource = null,
                enabled = true,
                onClickLabel = null,
                role = null
            )
            .background(MaterialTheme.colorScheme.background)
    ) {
        val _frameCount: Long = viewModel.frameCount

        viewModel.square.let { obj ->
            drawRect(
                color = squareColor, topLeft = Offset(obj.X, obj.Y), size = Size(obj.size, obj.size)
            )
        }

        viewModel.dot.let { obj ->
            drawRect(
                color = dotColor, topLeft = Offset(obj.X, obj.Y), size = Size(obj.size, obj.size)
            )
        }
    }

    if (viewModel.gameOver) {
        Overlay(viewModel)
    }
}

@Composable
fun Overlay(viewModel: GameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Game Over", color = Color.White, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Survived: ${viewModel.elapsedMS / 1000f}s",
                color = Color.White,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                viewModel.reset()

            }) {
                Text("Restart")
            }
        }
    }
}
