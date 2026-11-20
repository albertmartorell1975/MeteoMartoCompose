package com.martorell.albert.meteomartocompose.ui.designsystem.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

private const val THEME_GROUP = "Themes"
private const val LAYOUT_GROUP = "Layout"
private const val ACCESSIBILITY_GROUP = "Accessibility"

private const val RTL_LOCALE = "ar"
private const val MAX_FONT_SCALE = 2.0f

private const val PHONE_DEVICE = "spec:width=411dp,height=891dp"
private const val TABLET_DEVICE = "spec:width=1280dp,height=800dp,dpi=240"

@Preview(name = "Light Mode", group = THEME_GROUP, showBackground = true)
@Preview(name = "Dark Mode", group = THEME_GROUP, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "RTL", group = LAYOUT_GROUP, locale = RTL_LOCALE)
@Preview(name = "Large Font", group = ACCESSIBILITY_GROUP, fontScale = MAX_FONT_SCALE)
annotation class MmPreview

@Preview(name = "Phone", device = PHONE_DEVICE)
@Preview(name = "Tablet", device = TABLET_DEVICE)
annotation class MmDevicePreview
