package aoc2025.day08

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Circle(
    modifier: Modifier = Modifier,
    radius: Dp = 20.dp,
    color: Color = Color.Green,
    style: DrawStyle = Fill
) {
    Canvas(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        drawCircle(
            color = color,
            radius = radius.value,
            style = style
        )
    }
}
