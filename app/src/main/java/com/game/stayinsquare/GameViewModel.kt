package com.game.stayinsquare

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var frameCount by mutableLongStateOf(0L)
        private set

    val dot = GameObject(
        "dot", 20f, Direction.NORTH, 300f, 300f, 0f, 0f
    )
    val square = GameObject(
        "square", 400f, Direction.NORTH, 100f, 100f, 1f, 1f
    )
    var elapsedMS by mutableLongStateOf(0L)

    var gameOver by mutableStateOf(false)
        private set
    var isRunning by mutableStateOf(false)

    private fun isGameOver(): Boolean {
        return (dot.X + dot.size > square.X + square.size ||
                dot.X < square.X ||
                dot.Y + dot.size > square.Y + square.size ||
                dot.Y < square.Y)
    }

    fun update(deltaMS: Long, screenWidth: Float, screenHeight: Float) {
        if (gameOver) return

        dot.update(screenWidth, screenHeight)
        square.update(screenWidth, screenHeight)
        elapsedMS += deltaMS
        frameCount++

        if (isGameOver()) gameOver = true
    }

    fun reset() {
        gameOver = false
        dot.X = 300f
        dot.Y = 300f
        dot.vX = 0f
        dot.vY = 0f
        dot.direction = Direction.NORTH

        square.X = 100f
        square.Y = 100f
        square.direction = Direction.NORTH

        elapsedMS = 0L
        frameCount = 0L
        isRunning = false
        dot.trail.clear()
    }
}
