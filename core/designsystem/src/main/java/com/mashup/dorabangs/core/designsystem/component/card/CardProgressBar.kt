package com.mashup.dorabangs.core.designsystem.component.card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.dorabangs.core.designsystem.theme.DoraColorTokens
import com.mashup.dorabangs.core.designsystem.theme.DoraGradientToken
import com.mashup.dorabangs.core.designsystem.theme.DoraRoundTokens

@Composable
fun CardProgressBar(
    completedColor: Color,
    remainColor: Brush,
    modifier: Modifier = Modifier,
    timeInProgress: Float = 0f,
) {
    var targetPercentage by remember { mutableFloatStateOf(0.0f) }

    val animatedPercentage by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 8000),
        label = "",
    )

    LaunchedEffect(Unit) {
        targetPercentage = 0.8f - timeInProgress
    }

    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(timeInProgress + animatedPercentage)
                .fillMaxHeight()
                .background(
                    color = completedColor,
                    shape = DoraRoundTokens.Round99,
                ),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    brush = remainColor,
                    shape = DoraRoundTokens.Round99,
                ),
        )
    }
}

@Preview
@Composable
fun PreviewCardProgressBar() {
    CardProgressBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp),
        completedColor = DoraColorTokens.Black,
        remainColor = DoraGradientToken.Gradient2,
    )
}
