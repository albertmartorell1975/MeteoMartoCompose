package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndMotion
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import kotlin.math.roundToInt

/**
 * Themed Material 3 `OutlinedTextField` wired to [MeteoMartoTheme.colors] and input shapes,
 * performing a horizontal error-shake animation when [isError] transitions to `true`
 * outside inspection mode.
 *
 * @param value Current input text value.
 * @param onValueChange Callback invoked with updated text when the user edits the field.
 * @param modifier Modifier applied directly to the `OutlinedTextField`.
 * @param label Optional floating label composable.
 * @param placeholder Optional placeholder composable shown when [value] is empty.
 * @param leadingIcon Optional icon displayed at the start of the text field.
 * @param trailingIcon Optional icon displayed at the end of the text field.
 * @param supportingText Optional helper or validation error text displayed below the field.
 * @param isError Whether the input is currently in a validation error state.
 * @param visualTransformation Visual filter applied to the input (such as password masking).
 * @param keyboardOptions Software keyboard configuration options.
 * @param keyboardActions Callbacks triggered by IME keyboard actions.
 * @param enabled Whether the text field accepts user input.
 * @param singleLine Whether input is constrained to a single horizontally scrolling line.
 * @param shape Outline corner shape of the text field container.
 */
@Composable
fun MmTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    shape: Shape = MeteoMartoTheme.shapes.input,
) {
    val shakeOffset = remember { Animatable(0f) }
    val isInspectionMode = LocalInspectionMode.current

    LaunchedEffect(isError, isInspectionMode) {
        if (isError && !isInspectionMode) {
            shakeOffset.animateTo(
                targetValue = FndMotion.ErrorShake.OFFSET_POSITIVE,
                animationSpec = FndMotion.SpringExpressiveFloat,
            )
            shakeOffset.animateTo(
                targetValue = FndMotion.ErrorShake.OFFSET_NEGATIVE,
                animationSpec = FndMotion.SpringExpressiveFloat,
            )
            shakeOffset.animateTo(
                targetValue = FndMotion.ErrorShake.OFFSET_ZERO,
                animationSpec = FndMotion.SpringExpressiveFloat,
            )
        }
    }

    val colors = MeteoMartoTheme.colors

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            disabledTextColor = colors.onSurfaceVariant,
            errorTextColor = colors.onSurface,
            focusedContainerColor = colors.surface,
            unfocusedContainerColor = colors.surface,
            disabledContainerColor = colors.surfaceContainerLow,
            errorContainerColor = colors.surface,
            cursorColor = colors.primary,
            errorCursorColor = colors.error,
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline,
            disabledBorderColor = colors.outlineVariant,
            errorBorderColor = colors.error,
            focusedLeadingIconColor = colors.onSurfaceVariant,
            unfocusedLeadingIconColor = colors.onSurfaceVariant,
            errorLeadingIconColor = colors.error,
            focusedTrailingIconColor = colors.onSurfaceVariant,
            unfocusedTrailingIconColor = colors.onSurfaceVariant,
            errorTrailingIconColor = colors.error,
            focusedLabelColor = colors.primary,
            unfocusedLabelColor = colors.onSurfaceVariant,
            disabledLabelColor = colors.onSurfaceVariant,
            errorLabelColor = colors.error,
            focusedPlaceholderColor = colors.onSurfaceVariant,
            unfocusedPlaceholderColor = colors.onSurfaceVariant,
            focusedSupportingTextColor = colors.onSurfaceVariant,
            unfocusedSupportingTextColor = colors.onSurfaceVariant,
            errorSupportingTextColor = colors.error,
        ),
    )
}

@MmPreview
@Composable
private fun MmTextFieldPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            Column(
                modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
            ) {
                MmTextField(
                    value = "albert@meteomarto.com",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { MmText.LabelSmall("Email") },
                )
                MmTextField(
                    value = "invalid-email",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { MmText.LabelSmall("Email") },
                    supportingText = { MmText.LabelSmall("Invalid email format") },
                    isError = true,
                )
                MmTextField(
                    value = "Disabled field",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { MmText.LabelSmall("Read only") },
                    enabled = false,
                )
            }
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmTextFieldPalettesPreview() {
    MmDynamicPalettesShowcase {
        MmTextField(
            value = "Sabadell",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(MeteoMartoTheme.spacing.medium),
            label = { MmText.LabelSmall("City") },
        )
    }
}
