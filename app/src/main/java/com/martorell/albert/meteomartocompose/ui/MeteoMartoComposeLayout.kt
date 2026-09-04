package com.martorell.albert.meteomartocompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * This is the entry point for our app's visual structure.
 *
 * Think of this as a "wrapper" or a "container" that sets the rules for how everything
 * inside it should look. It does two main things:
 * 1. It applies the **MeteoMartoTheme**, which contains our custom colors, spaces, and fonts.
 * 2. It handles **dynamic scaling** (like zoom), so if the user wants larger text,
 *    this layout ensures every component inside it grows correctly.
 *
 * @param fontScale The "zoom level" for the app (1.0 is normal size).
 * @param content The actual UI parts (like screens or buttons) that will be placed inside this wrapper.
 */
@Composable
fun MeteoMartoComposeLayout(
    modifier: Modifier = Modifier,
    fontScale: Float = UserPreferences.DEFAULT_FONT_SCALE,
    content: @Composable () -> Unit,
) {

    MeteoMartoTheme(fontScale = fontScale) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            content()
        }
    }

}
