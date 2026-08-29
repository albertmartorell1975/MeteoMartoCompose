package com.martorell.albert.meteomartocompose.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

/**
 * MMMotion defines the standard motion and animation tokens for the MeteoMarto Design System.
 * Centralizing these ensure that every component re-bounds and feels exactly the same.
 */
object MMMotion {
    /**
     * Standard spring physics for expressive UI components (buttons, icons).
     */
    val SpringExpressive = spring<Int>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    /**
     * Animation labels for tools like Layout Inspector.
     */
    object Labels {
        const val BUTTON_WEIGHT = "ButtonWeightAnimation"
    }
}
