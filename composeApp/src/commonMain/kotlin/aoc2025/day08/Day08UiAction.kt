package aoc2025.day08

sealed interface Day08UiAction {

    object OnFindClustersClicked : Day08UiAction
    object OnDeleteClustersClicked : Day08UiAction
    data class OnFieldClicked(val x: Int, val y: Int) : Day08UiAction
}
