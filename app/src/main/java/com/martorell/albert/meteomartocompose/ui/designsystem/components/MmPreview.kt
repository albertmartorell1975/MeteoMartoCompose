package com.martorell.albert.meteomartocompose.ui.designsystem.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

private const val THEME_GROUP = "Themes"
private const val LAYOUT_GROUP = "Layout"
private const val ACCESSIBILITY_GROUP = "Accessibility"

private const val RTL_LOCALE = "ar"
private const val MAX_FONT_SCALE = 2.0f

private const val PHONE_DEVICE = "spec:width=411dp,height=891dp"
private const val PHONE_LANDSCAPE_DEVICE = "spec:width=891dp,height=411dp,orientation=landscape"
private const val TABLET_DEVICE = "spec:width=1280dp,height=800dp,dpi=240"
private const val TABLET_PORTRAIT_DEVICE = "spec:width=800dp,height=1280dp,dpi=240"
private const val FOLDABLE_DEVICE = "spec:width=673dp,height=841dp"
private const val FOLDABLE_LANDSCAPE_DEVICE = "spec:width=841dp,height=673dp,orientation=landscape"

@Preview(name = "Light Mode", group = THEME_GROUP, showBackground = true)
@Preview(name = "Dark Mode", group = THEME_GROUP, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "RTL", group = LAYOUT_GROUP, locale = RTL_LOCALE)
@Preview(name = "Large Font", group = ACCESSIBILITY_GROUP, fontScale = MAX_FONT_SCALE)
annotation class MmPreview

@Preview(name = "Phone", device = PHONE_DEVICE)
@Preview(name = "Phone Landscape", device = PHONE_LANDSCAPE_DEVICE)
@Preview(name = "Tablet", device = TABLET_DEVICE)
@Preview(name = "Tablet Portrait", device = TABLET_PORTRAIT_DEVICE)
@Preview(name = "Foldable", device = FOLDABLE_DEVICE)
@Preview(name = "Foldable Landscape", device = FOLDABLE_LANDSCAPE_DEVICE)
annotation class MmDevicePreview
