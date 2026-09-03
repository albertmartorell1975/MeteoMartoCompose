package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun MmLoadingOverlay(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MeteoMartoTheme.colors.surface.copy(alpha = 0.5f),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(enabled = true, onClick = {})
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MeteoMartoTheme.colors.primary
        )
    }
}

@MmPreview
@Composable
private fun MmLoadingOverlayPreview() {
    MeteoMartoTheme {
        MmLoadingOverlay()
    }
}
