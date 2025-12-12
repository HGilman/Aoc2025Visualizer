package aoc2025.day08

import androidx.compose.ui.graphics.Color
import aoc2025.utils.Point2d

/**
 * points here is not in Pixels or Dp, but in our own coordinates system
 * */
data class Day08UiState(
    val points: List<Point2d> = testPoints,
    val coloredClusters: List<Pair<Set<Point2d>, Color>> = emptyList(),
    val isFindingClusters: Boolean = false
)

private val testPoints: List<Point2d> = listOf(
    7 to 1,
    11 to 1,
    11 to 7,
    9 to 7,
    9 to 5,
    2 to 5,
    2 to 3,
    7 to 3
).map {
    it.toPoint2d()
}

fun Pair<Int, Int>.toPoint2d() = Point2d(first.toDouble(), second.toDouble())

private val colorRed = Color(red = 255, green = 0, blue = 0, alpha = 255)
private val colorBlue = Color(red = 0, green = 0, blue = 255, alpha = 255)
private val colorYellow = Color(red = 255, green = 255, blue = 0, alpha = 255)
private val colorPurple = Color(red = 197, green = 0, blue = 252, alpha = 255)
private val colorOrange = Color(red = 255, green = 132, blue = 2, alpha = 255)
private val colorCyan = Color(red = 5, green = 235, blue = 238, alpha = 255)
private val colorBlack = Color(red = 0, green = 0, blue = 0, alpha = 255)

private val clustersColors = listOf(
    colorRed,
    colorBlue,
    colorYellow,
    colorPurple,
    colorOrange,
    colorCyan,
    colorBlack
)

fun provideColorForCluster(i: Int): Color {
    return if (i < clustersColors.size) {
        clustersColors[i]
    } else {
        colorBlack
    }
}
