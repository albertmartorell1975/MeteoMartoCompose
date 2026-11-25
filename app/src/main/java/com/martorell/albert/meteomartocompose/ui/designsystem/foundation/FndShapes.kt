package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

private const val INPUT_RADIUS_DP = 8
private const val CARD_RADIUS_DP = 20
private const val DIALOG_RADIUS_DP = 28

/**
 * Corner shape tokens for the MeteoMarto design system.
 *
 * @property input Corner shape applied to text fields and compact input controls.
 * @property card Corner shape applied to weather cards and content containers.
 * @property dialog Corner shape applied to modal dialogs and large sheets.
 * @property pill Fully rounded pill shape applied to primary buttons and badges.
 */
@Immutable
data class FndShapes(
    val input: CornerBasedShape = RoundedCornerShape(INPUT_RADIUS_DP.dp),
    val card: CornerBasedShape = RoundedCornerShape(CARD_RADIUS_DP.dp),
    val dialog: CornerBasedShape = RoundedCornerShape(DIALOG_RADIUS_DP.dp),
    val pill: CornerBasedShape = CircleShape,
)

/**
 * Converts [FndShapes] foundation tokens into a Material 3 [Shapes] configuration.
 *
 * @return Material 3 [Shapes] instance backed by [FndShapes.input], [FndShapes.card], and [FndShapes.dialog].
 */
fun FndShapes.toMaterialShapes(): Shapes = Shapes(
    extraSmall = input,
    small = input,
    medium = card,
    large = dialog,
    extraLarge = dialog,
)

/**
 * CompositionLocal providing the active [FndShapes] tokens.
 */
val LocalFndShapes = staticCompositionLocalOf { FndShapes() }
