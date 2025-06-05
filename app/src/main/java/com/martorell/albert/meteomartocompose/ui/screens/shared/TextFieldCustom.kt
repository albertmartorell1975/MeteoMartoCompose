package com.martorell.albert.meteomartocompose.ui.screens.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.martorell.albert.meteomartocompose.R

/**
 * Its main purpose is build a TextField, which can be re-used. Maybe it will need some further minor customizations
 * @param value its current value that displays
 * @param title its label that explains the content
 * @param placeholder a detail explanation about the content to be typed
 * @param imeAction the type of action to be done
 * @param keyboardType the keyboard type that is needed
 * @param onValueChanged a lambda that is executed then the value is modified
 * @param passwordVisible a flag that says if the password must be visible or not
 * @param onTrailIconChange a lambda that is executed when the trailing icon is swapped
 */
@Composable
fun TextFieldCustom(
    value: String,
    @StringRes title: Int,
    @StringRes placeholder: Int,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit,
    passwordVisible: Boolean = false,
    onTrailIconChange: (Boolean) -> Unit?
) {

    TextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        label = { Text(text = stringResource(id = title)) },
        placeholder = { Text(text = stringResource(id = placeholder)) },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),

        visualTransformation =
            if (keyboardType == KeyboardType.Password)
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation()
            else
                VisualTransformation.None,
        trailingIcon = {
            if (keyboardType == KeyboardType.Password) {
                IconToggleButton(
                    checked = passwordVisible,
                    onCheckedChange = { onTrailIconChange(it) }
                ) {
                    Icon(
                        imageVector =
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = stringResource(R.string.visibility_password)
                    )
                }
            }

        }
    )
}