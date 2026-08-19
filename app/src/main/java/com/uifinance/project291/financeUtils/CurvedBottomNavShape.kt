package com.uifinance.project291.financeUtils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class CurvedBottomNavShape(
    private val curveRadius: Float,
    private val curveDepth: Float,
    private val curveSmoothness: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val centerX = size.width / 2f

            moveTo(0f, 0f)

            // 1. Line to the start of the curve
            lineTo(centerX - curveRadius, 0f)

            // 2. Smoother Cubic transition into the pocket
            cubicTo(
                x1 = centerX - curveRadius + curveSmoothness, y1 = 0f,
                x2 = centerX - curveSmoothness, y2 = curveDepth,
                x3 = centerX, y3 = curveDepth
            )

            // 3. Smoother Cubic transition back up to the flat line
            cubicTo(
                x1 = centerX + curveSmoothness, y1 = curveDepth,
                x2 = centerX + curveRadius - curveSmoothness, y2 = 0f,
                x3 = centerX + curveRadius, y3 = 0f
            )

            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}
