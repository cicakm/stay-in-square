package com.game.stayinsquare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    MyScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MyScreen(modifier: Modifier) {
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        var lastFrameNanos = 0L
        while (isRunning) {
            withFrameNanos { frameTimeNanos ->
                val deltaMs =
                    if (lastFrameNanos == 0L) 0L else (frameTimeNanos - lastFrameNanos) / 1_000_000L
                lastFrameNanos = frameTimeNanos
                elapsedMs += deltaMs
            }
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(
                style = TextStyle(
                    fontSize = 48.sp,
                    fontFamily = FontFamily.Monospace,
                    fontFeatureSettings = "tnum"
                ), text = "Elapsed time: ${elapsedMs / 1000f}"
            )
        }
        Row(modifier = modifier) {
            Button(modifier = modifier, onClick = {
                isRunning = !isRunning
            }) { Text(text = "Start/stop") }
            Button(modifier = modifier, onClick = {
                elapsedMs = 0L
            }, enabled = !isRunning) { Text(text = "Reset") }
        }
    }
}
