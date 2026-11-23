package com.martorell.albert.meteomartocompose.ui.designsystem

import com.github.takahirom.roborazzi.AndroidComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester.TestParameter.JUnit4TestParameter.AndroidPreviewJUnit4TestParameter
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage

@OptIn(ExperimentalRoborazziApi::class)
class RoborazziPreviewTester :
    ComposePreviewTester<AndroidPreviewJUnit4TestParameter> by AndroidComposePreviewTester(
        capturer = AndroidComposePreviewTester.Capturer { parameter ->
            parameter.preview.captureRoboImage(
                filePath = parameter.filePath,
                roborazziOptions = parameter.roborazziOptions.copy(
                    compareOptions = RoborazziOptions.CompareOptions(
                        changeThreshold = CROSS_OS_ANTIALIASING_THRESHOLD,
                    ),
                ),
                roborazziComposeOptions = parameter.roborazziComposeOptions,
            )
        },
    ) {
    companion object {
        private const val CROSS_OS_ANTIALIASING_THRESHOLD = 0.01f
    }
}
