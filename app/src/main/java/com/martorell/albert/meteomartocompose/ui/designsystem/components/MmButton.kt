package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
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
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndFontWeight
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndMotion
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.RobotoFlex

@Composable
fun MmPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
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

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        contentPadding = ButtonDefaults.ContentPadding,
    ) {
        val expressiveTextStyle = LocalTextStyle.current.copy(
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(animatedWeight),
        )

        CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
            content()
        }
    }
}

@Composable
fun MmSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val targetWeight = if (isPressed || isFocused) FndFontWeight.SEMIBOLD else FndFontWeight.NORMAL
    val animatedWeight by animateIntAsState(
        targetValue = targetWeight,
        animationSpec = FndMotion.SpringExpressiveInt,
        label = FndMotion.Labels.BUTTON_WEIGHT,
    )

    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        val expressiveTextStyle = LocalTextStyle.current.copy(
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(animatedWeight),
        )
        CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
            content()
        }
    }
}

@Composable
fun MmTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val targetWeight = if (isPressed || isFocused) FndFontWeight.SEMIBOLD else FndFontWeight.NORMAL
    val animatedWeight by animateIntAsState(
        targetValue = targetWeight,
        animationSpec = FndMotion.SpringExpressiveInt,
        label = FndMotion.Labels.BUTTON_WEIGHT,
    )

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        val expressiveTextStyle = LocalTextStyle.current.copy(
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(animatedWeight),
        )
        CompositionLocalProvider(LocalTextStyle provides expressiveTextStyle) {
            content()
        }
    }
}

@MmPreview
@Composable
private fun MmPrimaryButtonPreview() {
    MeteoMartoTheme {
        MmPrimaryButton(onClick = {}) {
            Text("Primary Action")
        }
    }
}

@MmPreview
@Composable
private fun MmSecondaryButtonPreview() {
    MeteoMartoTheme {
        MmSecondaryButton(onClick = {}) {
            Text("Secondary Action")
        }
    }
}

@MmPreview
@Composable
private fun MmTertiaryButtonPreview() {
    MeteoMartoTheme {
        MmTertiaryButton(onClick = {}) {
            Text("Tertiary Action")
        }
    }
}

@MmPreview
@Composable
private fun MmButtonStatesPreview() {
    MeteoMartoTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
            modifier = Modifier.padding(MeteoMartoTheme.spacing.medium)
        ) {
            MmPrimaryButton(onClick = {}, enabled = true) { Text("Primary Enabled") }
            MmPrimaryButton(onClick = {}, enabled = false) { Text("Primary Disabled") }
            MmSecondaryButton(onClick = {}, enabled = true) { Text("Secondary Enabled") }
            MmSecondaryButton(onClick = {}, enabled = false) { Text("Secondary Disabled") }
        }
    }
}
