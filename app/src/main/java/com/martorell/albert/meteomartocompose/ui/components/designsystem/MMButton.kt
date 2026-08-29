package com.martorell.albert.meteomartocompose.ui.components.designsystem

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.martorell.albert.meteomartocompose.ui.theme.MMFontWeight
import com.martorell.albert.meteomartocompose.ui.theme.MMMotion
import com.martorell.albert.meteomartocompose.ui.theme.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.theme.RobotoFlex

/**
 * [MMPrimaryButton] is the high-emphasis action component for the MeteoMarto Design System.
 * Use it for the primary action of a screen.
 *
 * It features "Expressive Motion" by dynamically adjusting its font weight using
 * the Roboto Flex variable font and spring physics during user interactions.
 */
@Composable
fun MMPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val targetWeight = if (isPressed || isFocused) {
        MMFontWeight.SEMIBOLD
    } else {
        MMFontWeight.NORMAL
    }

    val animatedWeight by animateIntAsState(
        targetValue = targetWeight,
        animationSpec = MMMotion.SpringExpressive,
        label = MMMotion.Labels.BUTTON_WEIGHT
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        contentPadding = ButtonDefaults.ContentPadding,
    ) {
        val expressiveTextStyle = LocalTextStyle.current.copy(
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(animatedWeight)
        )

        CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
            content()
        }
    }
}

/**
 * [MMSecondaryButton] is the medium-emphasis action component.
 * Use it for supporting actions that are not the primary focus.
 */
@Composable
fun MMSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val targetWeight = if (isPressed || isFocused) MMFontWeight.SEMIBOLD else MMFontWeight.NORMAL
    val animatedWeight by animateIntAsState(
        targetValue = targetWeight,
        animationSpec = MMMotion.SpringExpressive,
        label = MMMotion.Labels.BUTTON_WEIGHT
    )

    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        val expressiveTextStyle = LocalTextStyle.current.copy(
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(animatedWeight)
        )
        CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
            content()
        }
    }
}

/**
 * [MMTertiaryButton] is the low-emphasis action component.
 * Use it for auxiliary actions, cancel buttons, or secondary navigation.
 */
@Composable
fun MMTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val targetWeight = if (isPressed || isFocused) MMFontWeight.SEMIBOLD else MMFontWeight.NORMAL
    val animatedWeight by animateIntAsState(
        targetValue = targetWeight,
        animationSpec = MMMotion.SpringExpressive,
        label = MMMotion.Labels.BUTTON_WEIGHT
    )

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        val expressiveTextStyle = LocalTextStyle.current.copy(
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(animatedWeight)
        )
        CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
            content()
        }
    }
}

// --- Previews ---

@Preview(showBackground = true)
@Composable
private fun MMPrimaryButtonPreview() {
    MeteoMartoTheme {
        MMPrimaryButton(onClick = {}) {
            Text("Primary Action")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MMSecondaryButtonPreview() {
    MeteoMartoTheme {
        MMSecondaryButton(onClick = {}) {
            Text("Secondary Action")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MMTertiaryButtonPreview() {
    MeteoMartoTheme {
        MMTertiaryButton(onClick = {}) {
            Text("Tertiary Action")
        }
    }
}
