package aoc2025.utils

import kotlin.math.pow
import kotlin.math.sqrt

data class Point2d(
    val x: Double,
    val y: Double
) {
    fun distanceTo(other: Point2d): Double {
        return sqrt((other.x - x).pow(2) + (other.y - y).pow(2))
    }
}