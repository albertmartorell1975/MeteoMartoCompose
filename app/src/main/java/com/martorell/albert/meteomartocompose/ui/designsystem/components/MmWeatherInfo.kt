package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
 * A unified weather information component for the design system.
 *
 * This component is used across multiple screens (e.g., CityWeather and FavoritesDetail)
 * to ensure consistency in weather data presentation.
 *
 * Following design-system-governance:
 * - Stateless.
 * - Modifier is the first optional parameter.
 * - Uses MmText for typography.
 *
 * @param city The city weather domain model.
 * @param isHighTempAlertActive Whether to show the high temperature warning icon.
 * @param modifier Applied to the root layout.
 * @param titleFontWeight Optional font weight for the city name.
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
            fontWeight = titleFontWeight
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(city.weatherIcon)
                .crossfade(enable = true)
                .build(),
            contentDescription = stringResource(R.string.weather_icon_description),
            modifier = Modifier.size(dimensionResource(R.dimen.weather_icon_size)),
            contentScale = ContentScale.Crop
        )

        city.weatherDescription?.let {
            MmText.HeadlineMedium(
                text = it,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MmText.BodyLarge(
                text = stringResource(
                    R.string.city_current_temperature,
                    city.temperature.toString()
                ),
                textAlign = TextAlign.Center
            )
            if (isHighTempAlertActive) {
                Spacer(Modifier.width(MeteoMartoTheme.spacing.small))
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.high_temp_alert_icon_description),
                    tint = MeteoMartoTheme.colors.hot,
                    modifier = Modifier.size(MeteoMartoTheme.spacing.large)
                )
            }
        }
        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
        MmText.BodyMedium(
            text = stringResource(R.string.city_max_temperature, city.temperatureMax.toString()),
            color = MeteoMartoTheme.colors.hot,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
        MmText.BodyMedium(
            text = stringResource(R.string.city_min_temperature, city.temperatureMin.toString()),
            color = MeteoMartoTheme.colors.cold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
        MmText.BodyMedium(
            text = stringResource(R.string.city_pressure, city.pressure.toString()),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
        MmText.BodyMedium(
            text = stringResource(R.string.city_rain, city.rain.toString()),
            color = MeteoMartoTheme.colors.cold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
    }
}

@OptIn(ExperimentalCoilApi::class)
@MmPreview
@MmDevicePreview
@Composable
private fun MmWeatherInfoPreview() {
    val dummyCity = CityWeatherDomain(
        name = "Sabadell",
        temperature = 30.5,
        temperatureMax = 32.0,
        temperatureMin = 22.0,
        weatherDescription = "Sunny",
        weatherIcon = "https://openweathermap.org/img/wn/01d@2x.png",
        pressure = 1012,
        rain = 0.0
    )

    MeteoMartoTheme {
        androidx.compose.runtime.CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewAsyncImageCoil) {
            Column {
                MmWeatherInfo(
                    city = dummyCity,
                    isHighTempAlertActive = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
