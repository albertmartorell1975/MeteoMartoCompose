package com.martorell.albert.meteomartocompose.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

/**
 * MMMotion defines the standard motion and animation tokens for the MeteoMarto Design System.
 * Centralizing these ensure that every component re-bounds and feels exactly the same.
 */
object MMMotion {
    /**
     * Standard spring configuration for expressive UI components.
     */
    const val SPRING_DAMPING = Spring.DampingRatioMediumBouncy
    const val SPRING_STIFFNESS = Spring.StiffnessMediumLow

    /**
     * Standard spring physics for expressive UI components (buttons, icons).
     */
    val SpringExpressiveInt = spring<Int>(
        dampingRatio = SPRING_DAMPING,
        stiffness = SPRING_STIFFNESS,
    )

    /**
     * Standard spring physics for float-based animations (offsets, alpha).
     */
    val SpringExpressiveFloat = spring<Float>(
        dampingRatio = SPRING_DAMPING,
        stiffness = SPRING_STIFFNESS,
    )

    /**
     * Animation labels for tools like Layout Inspector.
     */
    object Labels {
        const val BUTTON_WEIGHT = "ButtonWeightAnimation"
    }

    /**
     * Tokens for error feedback animations.
     * These values represent the horizontal pixel offset for the "shake" sequence.
     * 10px is chosen as a subtle but noticeable distance for "denial" feedback.
     */
    object ErrorShake {
        const val OFFSET_POSITIVE = 10f
        const val OFFSET_NEGATIVE = -10f
        const val OFFSET_ZERO = 0f
    }
}
