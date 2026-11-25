package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

private const val SCRIM_ALPHA = 0.5f

/**
 * Translucent blocking loading scrim displaying a centered progress indicator with a
 * localized TalkBack content description.
 *
 * @param modifier Modifier applied to the scrim container; callers own sizing such as `fillMaxSize()`.
 * @param backgroundColor Scrim background color rendered behind the progress indicator.
 */
@Composable
fun MmLoadingOverlay(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MeteoMartoTheme.colors.surface.copy(alpha = SCRIM_ALPHA),
) {
    val loadingDescription = stringResource(R.string.loading_content_description)

    Box(
        modifier = modifier
            .semantics { contentDescription = loadingDescription }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MeteoMartoTheme.colors.primary,
        )
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun MmLoadingOverlayPreview() {
    MeteoMartoTheme {
        MmLoadingOverlay(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
