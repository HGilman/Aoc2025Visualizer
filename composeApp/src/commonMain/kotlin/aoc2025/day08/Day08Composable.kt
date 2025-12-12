package aoc2025.day08

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import aoc2025.day08.Day08UiAction.OnFieldClicked
import aoc2025.utils.Point2d
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun Day08Composable(
    viewModel: Day08ViewModel = viewModel { Day08ViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Day08Screen(
        modifier = modifier,
        uiState = uiState,
        onFieldClicked = { x, y ->
            viewModel.onAction(OnFieldClicked(x = x, y))
        },
        onFindClustersButtonClicked = {
            viewModel.onAction(Day08UiAction.OnFindClustersClicked)
        },
        onDeleteClustersButtonClicked = {
            viewModel.onAction(Day08UiAction.OnDeleteClustersClicked)
        }
    )
}

@Composable
private fun Day08Screen(
    uiState: Day08UiState,
    onFieldClicked: (x: Int, y: Int) -> Unit = { _, _ -> Unit },
    onFindClustersButtonClicked: () -> Unit = {},
    onDeleteClustersButtonClicked: () -> Unit = {},
    delta: Int = 20,
    modifier: Modifier = Modifier,
    pointRadius: Int = 10,
) {
    val xStartOffset = delta.dp
    val yStartOffset = delta.dp
    var parentSize: IntSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .background(Color(215, 212, 212, 255))
            .onGloballyPositioned { coordinates ->
                parentSize = coordinates.size
            }
            .pointerInput(Unit) {
                detectTapGestures { (x: Float, y: Float) ->
                    val xCoordinates = ((x - xStartOffset.toPx()) / delta.dp.toPx()).roundToInt()
                    val yCoordinates = ((y - yStartOffset.toPx()) / delta.dp.toPx()).roundToInt()
                    onFieldClicked(xCoordinates, yCoordinates)
                }
            }
    ) {
        var offsetX = 0.dp
        val amount = min(parentSize.height, parentSize.width) / delta - 2
        repeat(amount) { i: Int ->
            // Vertical lines
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .offset(x = xStartOffset + offsetX)
                    .background(Color.Black.copy(alpha = 0.25f))
            ) {}
            // Horizontal lines
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .offset(y = xStartOffset + offsetX)
                    .background(Color.Black.copy(alpha = 0.25f))
            ) {}
            // xAxis
            Text(
                modifier = Modifier
                    .offset(xStartOffset + offsetX - 6.dp, y = yStartOffset - 5.dp)
                    .align(Alignment.TopStart)
                    .width(16.dp),
                text = "$i",
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            // yAxis
            if (i != 0) {
                Text(
                    modifier = Modifier
                        .offset(x = xStartOffset - 6.dp, y = yStartOffset + offsetX - 5.dp)
                        .align(Alignment.TopStart)
                        .width(16.dp),
                    text = "$i",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            offsetX += delta.dp
        }
        // x axis
        Box(
            modifier = Modifier
                .drawWithCache {
                    val path = Path().apply {
                        moveTo(xStartOffset.toPx(), yStartOffset.toPx())
                        lineTo(size.width, yStartOffset.toPx())
                        relativeLineTo(-delta.dp.toPx(), -delta.dp.toPx() / 4)
                        moveTo(size.width, yStartOffset.toPx())
                        relativeLineTo(-delta.dp.toPx(), delta.dp.toPx() / 4)
                    }
                    onDrawBehind {
                        drawPath(path = path, color = Color.Blue.copy(alpha = 0.5f), style = Stroke(width = 2f))
                    }
                }
                .fillMaxWidth()
        )
        // y axis
        Box(
            modifier = Modifier
                .drawWithCache {
                    val path = Path().apply {
                        moveTo(xStartOffset.toPx(), yStartOffset.toPx())
                        lineTo(xStartOffset.toPx(), size.height)
                        relativeLineTo(-delta.dp.toPx() / 4, -delta.dp.toPx())
                        moveTo(xStartOffset.toPx(), size.height)
                        relativeLineTo(delta.dp.toPx() / 4, -delta.dp.toPx())
                    }
                    onDrawBehind {
                        drawPath(path = path, color = Color.Blue.copy(alpha = 0.5f), style = Stroke(width = 2f))
                    }
                }
                .fillMaxHeight()
        )
        for (p in uiState.points) {
            Circle(
                modifier = Modifier.offset(
                    x = (p.x * delta).dp + xStartOffset,
                    y = (p.y * delta).dp + yStartOffset
                ),
                radius = pointRadius.dp
            )
        }
        val coloredClusters = uiState.coloredClusters
        for (cluster in coloredClusters) {
            for (point: Point2d in cluster.first) {
                Circle(
                    modifier = Modifier.offset(
                        x = (point.x * delta).dp + xStartOffset,
                        y = (point.y * delta).dp + yStartOffset
                    ),
                    radius = pointRadius.dp,
                    color = cluster.second,
                    style = Stroke(width = 6f)
                )
            }
        }
        Button(
            onClick = { onFindClustersButtonClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-50).dp)
        ) {
            Text("Find clusters")
        }
        Button(
            onClick = { onDeleteClustersButtonClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-4).dp)
        ) {
            Text("Delete clusters")
        }
        AnimatedVisibility(
            uiState.isFindingClusters,
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
    }

}

@Composable
@Preview
fun Day08ComposablePreview() {
    Day08Screen(
        uiState = Day08UiState(),
        modifier = Modifier
            .width(600.dp)
            .height(600.dp)
    )
}
