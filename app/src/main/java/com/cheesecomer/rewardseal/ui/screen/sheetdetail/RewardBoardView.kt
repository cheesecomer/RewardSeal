package com.cheesecomer.rewardseal.ui.screen.sheetdetail

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardseal.model.RewardSheet

fun connectionPoint(
    point: Offset,
    radius: Float,
    isExit: Boolean,
): Offset {
    return if (isExit) {
        point + Offset(0f, radius * 0.75f)
    } else {
        point + Offset(0f, -radius * 0.75f)
    }
}

@Composable
fun RewardBoardView(
    sheet: RewardSheet,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val visibleCells = 13
        val topMarginCells = 1
        val bottomMarginCells = 2
        val totalCells = maxOf(
            visibleCells,
            sheet.goalCount + topMarginCells + bottomMarginCells,
        )

        val cellHeight = this.maxHeight / visibleCells
        val boardHeight = cellHeight * totalCells
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boardHeight)
                    .padding(horizontal = 24.dp),
            ) {
                val cellHeightPx = size.height / totalCells

                val topMargin = cellHeightPx * topMarginCells
                val bottomMargin = cellHeightPx * bottomMarginCells

                val nodeRadius = cellHeightPx * 0.22f
                val goalRadius = cellHeightPx * 0.34f
                val strokeWidth = cellHeightPx * 0.06f

                val leftMargin = cellHeightPx
                val rightMargin = cellHeightPx

                val drawableWidth = size.width - leftMargin - rightMargin
                val drawableHeight = size.height - topMargin - bottomMargin

//                val stepY = drawableHeight / sheet.goalCount
                val stepY = drawableHeight / (sheet.goalCount - 1)

                val centerX = leftMargin + drawableWidth / 2f
                val amplitude = drawableWidth * 0.36f

                val points = (0 until sheet.goalCount).map { index ->
                    Offset(
                        x = centerX + if (index % 2 == 0) -amplitude else amplitude,
                        y = topMargin + index * stepY,
                    )
                }

                for (i in 0 until points.lastIndex) {
                    val startCenter = points[i]
                    val endCenter = points[i + 1]

                    val startRadius =
                        if (i == sheet.goalCount - 1) goalRadius else nodeRadius

                    val endRadius =
                        if (i + 1 == sheet.goalCount - 1) goalRadius else nodeRadius

                    val start = if (i == 0) {
                        startCenter
                    } else {
                        connectionPoint(
                            point = startCenter,
                            radius = startRadius,
                            isExit = true,
                        )
                    }

                    val end = if (i + 1 == sheet.goalCount - 1) {
                        endCenter
                    } else {
                        connectionPoint(
                            point = endCenter,
                            radius = endRadius,
                            isExit = false,
                        )
                    }

                    val direction = if (i % 2 == 0) 1f else -1f
                    val bend = cellHeightPx * 1.6f * direction
                    val verticalBend = stepY * 0.25f

                    val path = Path().apply {
                        moveTo(start.x, start.y)

                        cubicTo(
                            start.x + bend,
                            start.y + verticalBend,

                            end.x - bend,
                            end.y - verticalBend,

                            end.x,
                            end.y,
                        )
                    }

                    drawPath(
                        path = path,
                        color = Color.Gray.copy(alpha = 0.25f),
                        style = Stroke(
                            width = strokeWidth * 1.25f,
                            cap = StrokeCap.Round,
                        ),
                    )
                }

                points.forEachIndexed { index, point ->
                    val isStamped = index < sheet.currentCount
                    val isGoal = index == sheet.goalCount - 1
                    val radius = if (isGoal) goalRadius else nodeRadius

                    drawCircle(
                        color = when {
                            isGoal -> Color(0xFFFFD54F)
                            isStamped -> Color(0xFFFF8A80)
                            else -> Color.White
                        },
                        radius = radius,
                        center = point,
                    )

                    drawCircle(
                        color = Color.Black,
                        radius = radius,
                        center = point,
                        style = Stroke(width = strokeWidth * 1.5f),
                    )
                }
            }
        }
    }
}