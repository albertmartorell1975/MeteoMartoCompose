package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.utils.previewAsyncImageCoil

/**
 * Displays city weather header details (city title, remote weather condition icon,
 * condition description) and a tonal [MmCard] containing thermal and atmospheric telemetry
 * with an optional high-temperature alert indicator.
 *
 * @param city Domain weather snapshot providing city name, icon URL, and metrics.
 * @param isHighTempAlertActive Whether to show the high-temperature warning icon next to temperature.
 * @param modifier Modifier applied to the root vertical layout.
 * @param titleFontWeight Font weight override applied to the city display heading.
 */
@Composable
fun MmWeatherInfo(
    city: CityWeatherDomain,
    isHighTempAlertActive: Boolean,
    modifier: Modifier = Modifier,
    titleFontWeight: FontWeight = FontWeight.Normal,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MmText.DisplayLarge(
            text = city.name,
            textAlign = TextAlign.Center,
            fontWeight = titleFontWeight,
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

        val isInspectionMode = LocalInspectionMode.current

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(city.weatherIcon)
                .crossfade(enable = !isInspectionMode)
                .build(),
            contentDescription = stringResource(R.string.weather_icon_description),
            modifier = Modifier.size(MeteoMartoTheme.dimensions.weatherIconSize),
            contentScale = ContentScale.Crop,
        )

        city.weatherDescription?.let {
            MmText.HeadlineMedium(
                text = it,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

        MmCard {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.medium),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    MmText.BodyLarge(
                        text = stringResource(
                            R.string.city_current_temperature,
                            city.temperature.toString(),
                        ),
                        textAlign = TextAlign.Center,
                    )
                    if (isHighTempAlertActive) {
                        Spacer(Modifier.width(MeteoMartoTheme.spacing.small))
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = stringResource(R.string.high_temp_alert_icon_description),
                            tint = MeteoMartoTheme.colors.hot,
                            modifier = Modifier.size(MeteoMartoTheme.spacing.large),
                        )
                    }
                }
                MmText.BodyMedium(
                    text = stringResource(R.string.city_max_temperature, city.temperatureMax.toString()),
                    color = MeteoMartoTheme.colors.hot,
                    textAlign = TextAlign.Center,
                )
                MmText.BodyMedium(
                    text = stringResource(R.string.city_min_temperature, city.temperatureMin.toString()),
                    color = MeteoMartoTheme.colors.cold,
                    textAlign = TextAlign.Center,
                )
                MmText.BodyMedium(
                    text = stringResource(R.string.city_pressure, city.pressure.toString()),
                    textAlign = TextAlign.Center,
                )
                MmText.BodyMedium(
                    text = stringResource(R.string.city_rain, city.rain.toString()),
                    color = MeteoMartoTheme.colors.cold,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
    }
}

private const val DYNAMIC_PALETTES_PREVIEW_HEIGHT_DP = 1600

private val PreviewCityWeather = CityWeatherDomain(
    name = "Sabadell",
    temperature = 30.5,
    temperatureMax = 32.0,
    temperatureMin = 22.0,
    weatherDescription = "Sunny",
    weatherIcon = "https://openweathermap.org/img/wn/01d@2x.png",
    pressure = 1012,
    rain = 0.0,
)

@OptIn(ExperimentalCoilApi::class)
@MmPreview
@MmDevicePreview
@Composable
private fun MmWeatherInfoPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalAsyncImagePreviewHandler provides previewAsyncImageCoil,
            ) {
                MmWeatherInfo(
                    city = PreviewCityWeather,
                    isHighTempAlertActive = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MeteoMartoTheme.spacing.medium),
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview(
    name = "Dynamic Palettes",
    group = "Palettes",
    showBackground = true,
    heightDp = DYNAMIC_PALETTES_PREVIEW_HEIGHT_DP,
)
@Composable
private fun MmWeatherInfoPalettesPreview() {
    MmDynamicPalettesShowcase {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
            LocalAsyncImagePreviewHandler provides previewAsyncImageCoil,
        ) {
            MmWeatherInfo(
                city = PreviewCityWeather,
                isHighTempAlertActive = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MeteoMartoTheme.spacing.medium),
            )
        }
    }
}
