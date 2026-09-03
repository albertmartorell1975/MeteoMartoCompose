package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

object FndMotion {
    const val SPRING_DAMPING = Spring.DampingRatioMediumBouncy
    const val SPRING_STIFFNESS = Spring.StiffnessMediumLow

    val SpringExpressiveInt = spring<Int>(
        dampingRatio = SPRING_DAMPING,
        stiffness = SPRING_STIFFNESS,
    )

    val SpringExpressiveFloat = spring<Float>(
        dampingRatio = SPRING_DAMPING,
        stiffness = SPRING_STIFFNESS,
    )

    object Labels {
        const val BUTTON_WEIGHT = "ButtonWeightAnimation"
    }

    object ErrorShake {
        const val OFFSET_POSITIVE = 10f
        const val OFFSET_NEGATIVE = -10f
        const val OFFSET_ZERO = 0f
    }
}
