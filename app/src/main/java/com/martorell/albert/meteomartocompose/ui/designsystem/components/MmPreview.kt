package com.martorell.albert.meteomartocompose.ui.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.ExtremeAlertPalette
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.RainPalette
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.SunnyPalette

private const val THEME_GROUP = "Themes"
private const val LAYOUT_GROUP = "Layout"
private const val ACCESSIBILITY_GROUP = "Accessibility"
private const val PALETTES_GROUP = "Palettes"

private const val RTL_LOCALE = "ar"
private const val MAX_FONT_SCALE = 2.0f

private const val PHONE_DEVICE = "spec:width=411dp,height=891dp"
private const val TABLET_DEVICE = "spec:width=1280dp,height=800dp,dpi=240"

/**
 * Multi-preview annotation that renders Light Mode, Dark Mode, RTL (`ar`),
 * and 200% font-scale accessibility previews for Design System components.
 */
@Preview(name = "Light Mode", group = THEME_GROUP, showBackground = true)
@Preview(name = "Dark Mode", group = THEME_GROUP, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "RTL", group = LAYOUT_GROUP, showBackground = true, locale = RTL_LOCALE)
@Preview(name = "Large Font", group = ACCESSIBILITY_GROUP, showBackground = true, fontScale = MAX_FONT_SCALE)
annotation class MmPreview

/**
 * Multi-preview annotation that renders Phone (`411dp x 891dp`) and Tablet (`1280dp x 800dp`)
 * form-factor previews for adaptive Design System components.
 */
@Preview(name = "Phone", device = PHONE_DEVICE)
@Preview(name = "Tablet", device = TABLET_DEVICE)
annotation class MmDevicePreview

/**
 * Preview annotation for showcasing Design System components across dynamic weather palettes.
 */
@Preview(name = "Dynamic Palettes", group = PALETTES_GROUP, showBackground = true)
annotation class MmDynamicPalettesPreview

/**
 * Renders [content] vertically across the sunny, rain, and extreme-alert weather palettes,
 * wrapping each variant in [MeteoMartoTheme] and a themed background `Surface`.
 *
 * @param content Composable preview content rendered once per dynamic weather palette.
 */
@Composable
internal fun MmDynamicPalettesShowcase(
    content: @Composable () -> Unit,
) {
    Column {
        listOf(SunnyPalette, RainPalette, ExtremeAlertPalette).forEach { palette ->
            MeteoMartoTheme(palette = palette) {
                Surface(color = MeteoMartoTheme.colors.background) {
                    content()
                }
            }
        }
    }
}
