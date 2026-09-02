package com.martorell.albert.meteomartocompose.ui.components.designsystem

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

private const val THEME_GROUP = "Themes"
private const val LAYOUT_GROUP = "Layout"
private const val ACCESSIBILITY_GROUP = "Accessibility"

/**
 * Standard RTL locale (Arabic) for testing Right-to-Left layout support.
 */
private const val RTL_LOCALE = "ar"

/**
 * Maximum recommended font scale (200%) according to Android Accessibility Guidelines.
 */
private const val MAX_FONT_SCALE = 2.0f

/**
 * Device specifications following [Material 3 Window Size Classes](https://m3.material.io/foundations/layout/applying-layout/window-size-classes).
 */
private const val PHONE_DEVICE = "spec:width=411dp,height=891dp"
private const val PHONE_LANDSCAPE_DEVICE = "spec:width=891dp,height=411dp,orientation=landscape"
private const val TABLET_DEVICE = "spec:width=1280dp,height=800dp,dpi=240"
private const val TABLET_PORTRAIT_DEVICE = "spec:width=800dp,height=1280dp,dpi=240"
private const val FOLDABLE_DEVICE = "spec:width=673dp,height=841dp"
private const val FOLDABLE_LANDSCAPE_DEVICE = "spec:width=841dp,height=673dp,orientation=landscape"

/**
 * [MMPreview] is a Multipreview annotation that encapsulates the primary visual dimensions
 * defined in the Design System's "Pragmatic Snapshot" strategy.
 *
 * It validates:
 * 1. **Themes**: Light and Dark modes.
 * 2. **Layout Direction**: LTR and RTL (using [RTL_LOCALE]).
 * 3. **Font Scaling**: Standard (1.0x) and Accessibility High ([MAX_FONT_SCALE]).
 */
@Preview(name = "Light Mode", group = THEME_GROUP, showBackground = true)
@Preview(name = "Dark Mode", group = THEME_GROUP, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "RTL", group = LAYOUT_GROUP, locale = RTL_LOCALE)
@Preview(name = "Large Font", group = ACCESSIBILITY_GROUP, fontScale = MAX_FONT_SCALE)
annotation class MMPreview

/**
 * [MMDevicePreview] validates the adaptive behavior of components across different device sizes
 * defined by Material 3 Window Size Classes.
 */
@Preview(name = "Phone", device = PHONE_DEVICE)
@Preview(name = "Phone Landscape", device = PHONE_LANDSCAPE_DEVICE)
@Preview(name = "Tablet", device = TABLET_DEVICE)
@Preview(name = "Tablet Portrait", device = TABLET_PORTRAIT_DEVICE)
@Preview(name = "Foldable", device = FOLDABLE_DEVICE)
@Preview(name = "Foldable Landscape", device = FOLDABLE_LANDSCAPE_DEVICE)
annotation class MMDevicePreview
