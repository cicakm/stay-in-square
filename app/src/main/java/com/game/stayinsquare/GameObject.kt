package com.game.stayinsquare

import androidx.compose.ui.geometry.Offset

data class GameObject(
    val id: String,
    val size: Float,
    var direction: Direction = Direction.EAST,
    var X: Float,
    var Y: Float,
    var vX: Float,
    var vY: Float,
    val trail: ArrayDeque<Offset> = ArrayDeque(10)
) {
    fun update(screenWidth: Float, screenHeight: Float) {
        trail.addLast(Offset(X, Y))
        if (trail.size > 10) trail.removeFirst()
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
