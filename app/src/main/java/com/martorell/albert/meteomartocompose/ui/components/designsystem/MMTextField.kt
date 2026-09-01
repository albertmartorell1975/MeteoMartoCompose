package com.martorell.albert.meteomartocompose.ui.components.designsystem

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import com.martorell.albert.meteomartocompose.ui.theme.MMMotion
import com.martorell.albert.meteomartocompose.ui.theme.MeteoMartoTheme
import kotlin.math.roundToInt

/**
 * [MMTextField] is the standardized input component for the MeteoMarto Design System.
 *
 * Following the Design System's pillars and Architecture Audit v2:
 * - **Typographic Boundary**: Encourages the use of [MMText] for labels and supporting messages.
 * - **Expressive Motion**: Features a "shake" animation when [isError] is triggered.
 * - **Stateless by Contract**: All input and error states are hoisted.
 * - **Slot API Pattern**: Flexible slots for icons and labels.
 *
 * @param value The current text to display in the field.
 * @param onValueChange Callback triggered when the text changes.
 * @param modifier Optional [Modifier] for layout.
 * @param label Optional Composable slot for the field label.
 * @param placeholder Optional Composable slot for the placeholder text.
 * @param leadingIcon Optional icon at the start of the field.
 * @param trailingIcon Optional icon at the end of the field.
 * @param supportingText Optional text below the field (e.g., error or hint).
 * @param isError Whether to display the field in an error state.
 * @param visualTransformation Controls the visual representation of the text (e.g., password masking).
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardActions Actions triggered by keyboard events.
 * @param enabled Whether the field is interactive.
 */
@Composable
fun MMTextField(
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
) {
    val shakeOffset = remember { Animatable(0f) }

    // Expressive Error Feedback: Shake the field when an error state is detected.
    LaunchedEffect(isError) {
        if (isError) {
            // Shake sequence: right -> left -> center using design system springs
            shakeOffset.animateTo(
                targetValue = MMMotion.ErrorShake.OFFSET_POSITIVE,
                animationSpec = MMMotion.SpringExpressiveFloat,
            )
            shakeOffset.animateTo(
                targetValue = MMMotion.ErrorShake.OFFSET_NEGATIVE,
                animationSpec = MMMotion.SpringExpressiveFloat,
            )
            shakeOffset.animateTo(
                targetValue = MMMotion.ErrorShake.OFFSET_ZERO,
                animationSpec = MMMotion.SpringExpressiveFloat,
            )
        }
    }

    Box(
        modifier = modifier.offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled,
            singleLine = true,
        )
    }
}

// --- Previews ---

@MMPreview
@Composable
private fun MMTextFieldPreview() {
    MeteoMartoTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
        ) {
            MMTextField(
                value = "Standard Input",
                onValueChange = {},
                label = { MMText.LabelSmall("Email") },
            )

            MMTextField(
                value = "Invalid input",
                onValueChange = {},
                isError = true,
                label = { MMText.LabelSmall("Password") },
                supportingText = { MMText.LabelSmall("Incorrect password") },
            )
        }
    }
}