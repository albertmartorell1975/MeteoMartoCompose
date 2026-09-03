package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndMotion
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import kotlin.math.roundToInt

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
) {
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isError) {
        if (isError) {
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

@MmPreview
@Composable
private fun MmTextFieldPreview() {
    MeteoMartoTheme {
        Column(
            modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
        ) {
            MmTextField(
                value = "Standard Input",
                onValueChange = {},
                label = { MmText.LabelSmall("Email") },
            )
        }
    }
}
