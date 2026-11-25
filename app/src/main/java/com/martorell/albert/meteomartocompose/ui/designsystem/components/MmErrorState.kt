package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Centered error recovery state displaying an illustration, headline message, optional
 * supporting description, and a primary retry action button.
 *
 * @param message Primary error headline displayed to the user.
 * @param onRetry Callback invoked when the retry action button is pressed.
 * @param modifier Modifier applied to the outer container; callers own root sizing constraints.
 * @param description Optional secondary explanation rendered below [message].
 * @param retryLabel Label displayed inside the retry button.
 * @param icon Custom illustration slot, defaulting to the forecast error graphic.
 */
@Composable
fun MmErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    retryLabel: String = stringResource(R.string.retry_action),
    icon: @Composable () -> Unit = {
        Image(
            painter = painterResource(R.drawable.ic_error_forecast),
            contentDescription = null,
            modifier = Modifier.size(MeteoMartoTheme.dimensions.errorIconSize),
        )
    },
) {
    Box(
        modifier = modifier.padding(MeteoMartoTheme.spacing.medium),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = MeteoMartoTheme.dimensions.maxContentWidth)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            icon()

            Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

            MmText.HeadlineMedium(
                text = message,
                textAlign = TextAlign.Center,
                color = MeteoMartoTheme.colors.onSurface,
            )

            description?.let {
                Spacer(Modifier.height(MeteoMartoTheme.spacing.small))
                MmText.BodyMedium(
                    text = it,
                    textAlign = TextAlign.Center,
                    color = MeteoMartoTheme.colors.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

            MmPrimaryButton(onClick = onRetry) {
                MmText.LabelLarge(text = retryLabel)
            }
        }
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun MmErrorStatePreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            MmErrorState(
                message = "Unable to load forecast",
                description = "Check your connection and try again.",
                onRetry = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmErrorStatePalettesPreview() {
    MmDynamicPalettesShowcase {
        MmErrorState(
            message = "Unable to load forecast",
            onRetry = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
