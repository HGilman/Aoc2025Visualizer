package aoc2025.day08

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(
        size = DpSize(600.dp, 600.dp),
        position = WindowPosition(300.dp, 300.dp),

    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "Aoc2025Visualizer",
        state = state,
        alwaysOnTop = true
    ) {
        App()
    }
}

@Composable
@Preview
fun App() {
    Day08Composable()
}
