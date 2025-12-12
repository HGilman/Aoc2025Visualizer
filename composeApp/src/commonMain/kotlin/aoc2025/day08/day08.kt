package aoc2025.day08

import aoc2025.utils.Point2d
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun largestClustersFlow(
    points: List<Point2d>,
    delay: Long = 400L
): Flow<List<Set<Point2d>>> {
    val shortestDistances = createDistances(points).sortedBy { it.distance }
    val largestClustersFlow = findLargestClusters(
        shortestLines = shortestDistances,
        amountOfPoints = points.size,
        delay = delay
    ).map { clusters: List<Set<Int>> ->
        clusters.map { cluster: Set<Int> ->
            cluster.map { index ->
                points[index]
            }.toSet()
        }
    }
    return largestClustersFlow
}

private fun createDistances(points: List<Point2d>): List<Line> {
    val size = points.size
    val lines = mutableListOf<Line>()
    for (i in 0 until size - 1) {
        for (j in i + 1 until size) {
            lines += Line(
                i = i,
                firstPoint = points[i],
                j = j,
                secondPoint = points[j]
            )
        }
    }
    return lines
}

private fun findLargestClusters(
    shortestLines: List<Line>,
    amountOfPoints: Int,
    delay: Long = 100L
): Flow<List<Set<Int>>> = flow {
    val clusters = mutableListOf<MutableSet<Int>>()

    for (line in shortestLines) {
        val i = line.i
        val j = line.j
        val iCluster: MutableSet<Int>? = clusters.find { it.contains(i) }
        val jCluster: MutableSet<Int>? = clusters.find { it.contains(j) }

        when {
            iCluster == null && jCluster == null -> {
                val newCluster = mutableSetOf(i, j)
                clusters.add(newCluster)
            }

            iCluster != null && jCluster == null -> {
                iCluster.add(i)
                iCluster.add(j)
            }

            iCluster == null && jCluster != null -> {
                jCluster.add(j)
                jCluster.add(i)
            }

            iCluster != null && jCluster != null -> {
                if (iCluster != jCluster) {
                    iCluster.addAll(jCluster)
                    clusters.remove(jCluster)
                }
            }
        }
        emit(clusters)
        if (clusters.size == 1 && clusters.first().size == amountOfPoints) {
            return@flow
        }
        delay(delay)
    }
}


data class Line(
    val i: Int,
    val firstPoint: Point2d,
    val j: Int,
    val secondPoint: Point2d
) {
    val distance: Double by lazy {
        secondPoint.distanceTo(firstPoint)
    }

    override fun toString(): String {
        return "i: $i, j: $j, x1: $firstPoint, x2: $secondPoint, d: $distance"
    }
}



























