package com.game.stayinsquare

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Direction {
    EAST, SOUTH, WEST, NORTH
}

data class GameObject(
    val id: String,
    val size: Float,
    var direction: Direction = Direction.EAST,
    var X: Float,
    var Y: Float,
    var vX: Float,
    var vY: Float
) {
    fun update(screenHeight: Float, screenWidth: Float) {
        X += vX
        Y += vY

        if (X < 0f) {
            X = 0f
            vX = -vX
        }
        if (X + size > screenWidth) {
            X = screenWidth - size
            vX = -vX
        }
        if (Y < 0f) {
            Y = 0f
            vY = -vY
        }
        if (Y + size > screenHeight) {
            Y = screenHeight - size
            vY = -vY
        }
    }


    fun changeDirection(speed: Float) {
        when (direction) {
            Direction.EAST -> {
                direction = Direction.SOUTH
                vX = 0f
                vY = speed
            }

            Direction.SOUTH -> {
                direction = Direction.WEST
                vX = -speed
                vY = 0f
            }

            Direction.WEST -> {
                direction = Direction.NORTH
                vX = 0f
                vY = -speed
            }

            else -> {
                direction = Direction.EAST
                vX = speed
                vY = 0f
            }
        }
    }
}

class GameViewModel : ViewModel() {
    var frameCount by mutableLongStateOf(0L)
        private set

    val dot = GameObject(
        "dot", 20f, Direction.EAST, 300f, 200f, 0f, 0f
    )
    val square = GameObject(
        "square", 400f, Direction.EAST, 100f, 100f, 1f, 1f
    )
    var elapsedMS by mutableLongStateOf(0L)
    fun update(deltaMS: Long, screenHeight: Float, screenWidth: Float) {
        dot.update(screenHeight, screenWidth)
        square.update(screenHeight, screenWidth)
        elapsedMS += deltaMS
        frameCount++
    }
}