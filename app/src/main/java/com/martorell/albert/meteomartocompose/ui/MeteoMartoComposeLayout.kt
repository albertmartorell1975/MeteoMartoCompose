package com.martorell.albert.meteomartocompose.ui

import androidx.compose.runtime.Composable
import com.martorell.albert.meteomartocompose.ui.theme.MeteoMartoComposeTheme

/**
 * Its goal is to apply the project them and "inflates" a composable class
 * @param content it is a composable class, usually a Scaffold one
 */
@Composable
fun MeteoMartoComposeLayout(content: @Composable () -> Unit) {

    MeteoMartoComposeTheme {
        content()
    }

}