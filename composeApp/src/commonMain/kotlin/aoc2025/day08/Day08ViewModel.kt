package aoc2025.day08

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aoc2025.day08.Day08UiAction.OnDeleteClustersClicked
import aoc2025.day08.Day08UiAction.OnFieldClicked
import aoc2025.day08.Day08UiAction.OnFindClustersClicked
import aoc2025.utils.Point2d
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Day08ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Day08UiState())
    val uiState = _uiState.asStateFlow()

    private var findClustersJob: Job? = null

    fun onAction(uiAction: Day08UiAction) {
        when (uiAction) {
            OnFindClustersClicked -> {
                onFindClustersClicked()
            }

            OnDeleteClustersClicked -> {
                onDeleteClustersClicked()
            }

            is OnFieldClicked -> {
                if (!_uiState.value.isFindingClusters) {
                    onFieldClicked(uiAction)
                }
            }
        }
    }

    private fun onDeleteClustersClicked() {
        findClustersJob?.cancel()
        _uiState.update {
            it.copy(
                coloredClusters = emptyList(),
                isFindingClusters = false
            )
        }
    }

    private fun onFindClustersClicked() {
        val clustersFlow = largestClustersFlow(
            uiState.value.points
        )
        findClustersJob?.cancel()
        _uiState.update {
            it.copy(isFindingClusters = true)
        }
        findClustersJob = viewModelScope.launch {
            println("Collecting clusters")
            clustersFlow
                .onEach { clusters: List<Set<Point2d>> ->
                    println("Clusters updated")
                    for (c in clusters) {
                        for (p in c) {
                            println(p)
                        }
                        println()
                    }
                }
                .onStart {
                    _uiState.update {
                        it.copy(isFindingClusters = true)
                    }
                }
                .onCompletion {
                    _uiState.update {
                        it.copy(isFindingClusters = false)
                    }
                }
                .collect { clusters: List<Set<Point2d>> ->
                    val coloredClusters = clusters.mapIndexed { i, cluster ->
                        cluster to provideColorForCluster(i = i)
                    }
                    _uiState.update {
                        it.copy(coloredClusters = coloredClusters)
                    }
                }
        }
    }

    private fun onFieldClicked(action: OnFieldClicked) {
        println("Taped: x: ${action.x}, y: ${action.y}")

        val newPoint = Point2d(action.x.toDouble(), action.y.toDouble())
        val currentPoints = _uiState.value.points
        val newPoints = if (currentPoints.contains(newPoint)) {
            currentPoints - newPoint
        } else {
            currentPoints + newPoint
        }
        _uiState.update {
            it.copy(points = newPoints)
        }
    }
}

