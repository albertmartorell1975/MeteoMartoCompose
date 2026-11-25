package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndFontWeight
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndMotion
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.RobotoFlex

/**
 * High-emphasis pill button for primary screen actions, enforcing a 48.dp minimum touch target
 * and expressive Roboto Flex font-weight animation on press or focus.
 *
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier `Modifier` applied to the button container.
 * @param enabled Whether the button is interactive.
 * @param interactionSource Optional hoisted interaction source for observing press and focus states.
 * @param content Composable label or icon row rendered inside the button.
 */
@Composable
fun MmPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val animatedWeight = rememberAnimatedButtonWeight(interactionSource)

    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = MeteoMartoTheme.dimensions.buttonHeight).minimumInteractiveComponentSize(),
        enabled = enabled,
        shape = MeteoMartoTheme.shapes.pill,
        colors = ButtonDefaults.buttonColors(
            containerColor = MeteoMartoTheme.colors.primary,
            contentColor = MeteoMartoTheme.colors.onPrimary,
        ),
        interactionSource = interactionSource,
        contentPadding = ButtonDefaults.ContentPadding,
    ) {
        ButtonExpressiveContent(
            animatedWeight = animatedWeight,
            content = content,
        )
    }
}

/**
 * Medium-emphasis tonal pill button for secondary actions, enforcing a 48.dp minimum touch target
 * and expressive Roboto Flex font-weight animation on press or focus.
 *
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier `Modifier` applied to the button container.
 * @param enabled Whether the button is interactive.
 * @param interactionSource Optional hoisted interaction source for observing press and focus states.
 * @param content Composable label or icon row rendered inside the button.
 */
@Composable
fun MmSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val animatedWeight = rememberAnimatedButtonWeight(interactionSource)

    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = MeteoMartoTheme.dimensions.buttonHeight).minimumInteractiveComponentSize(),
        enabled = enabled,
        shape = MeteoMartoTheme.shapes.pill,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MeteoMartoTheme.colors.secondaryContainer,
            contentColor = MeteoMartoTheme.colors.onSecondaryContainer,
        ),
        interactionSource = interactionSource,
    ) {
        ButtonExpressiveContent(
            animatedWeight = animatedWeight,
            content = content,
        )
    }
}

/**
 * Low-emphasis text button for tertiary or dismissive actions, enforcing a 48.dp minimum touch
 * target and expressive Roboto Flex font-weight animation on press or focus.
 *
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier `Modifier` applied to the button container.
 * @param enabled Whether the button is interactive.
 * @param interactionSource Optional hoisted interaction source for observing press and focus states.
 * @param content Composable label or icon row rendered inside the button.
 */
@Composable
fun MmTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val animatedWeight = rememberAnimatedButtonWeight(interactionSource)

    TextButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = MeteoMartoTheme.dimensions.buttonHeight).minimumInteractiveComponentSize(),
        enabled = enabled,
        shape = MeteoMartoTheme.shapes.pill,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MeteoMartoTheme.colors.primary,
        ),
        interactionSource = interactionSource,
    ) {
        ButtonExpressiveContent(
            animatedWeight = animatedWeight,
            content = content,
        )
    }
}

@Composable
private fun rememberAnimatedButtonWeight(
    interactionSource: MutableInteractionSource,
): Int {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val targetWeight = if (isPressed || isFocused) {
        FndFontWeight.SEMIBOLD
    } else {
        FndFontWeight.NORMAL
    }
    val animatedWeight by animateIntAsState(
        targetValue = targetWeight,
        animationSpec = FndMotion.SpringExpressiveInt,
        label = FndMotion.Labels.BUTTON_WEIGHT,
    )
    return animatedWeight
}

@Composable
private fun RowScope.ButtonExpressiveContent(
    animatedWeight: Int,
    content: @Composable RowScope.() -> Unit,
) {
    val expressiveTextStyle = LocalTextStyle.current.copy(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight(animatedWeight),
    )
    CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
        content()
    }
}

@MmPreview
@Composable
private fun MmButtonStatesPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
                modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
            ) {
                MmPrimaryButton(onClick = {}, enabled = true) { MmText.LabelLarge("Primary Enabled") }
                MmPrimaryButton(onClick = {}, enabled = false) { MmText.LabelLarge("Primary Disabled") }
                MmSecondaryButton(onClick = {}, enabled = true) { MmText.LabelLarge("Secondary Enabled") }
                MmSecondaryButton(onClick = {}, enabled = false) { MmText.LabelLarge("Secondary Disabled") }
                MmTertiaryButton(onClick = {}, enabled = true) { MmText.LabelLarge("Tertiary Enabled") }
                MmTertiaryButton(onClick = {}, enabled = false) { MmText.LabelLarge("Tertiary Disabled") }
            }
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmButtonPalettesPreview() {
    MmDynamicPalettesShowcase {
        Column(
            verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MeteoMartoTheme.spacing.medium),
        ) {
            MmPrimaryButton(onClick = {}) { MmText.LabelLarge("Primary Action") }
            MmSecondaryButton(onClick = {}) { MmText.LabelLarge("Secondary Action") }
            MmTertiaryButton(onClick = {}) { MmText.LabelLarge("Tertiary Action") }
        }
    }
}
