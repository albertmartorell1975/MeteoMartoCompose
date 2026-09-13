package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmErrorState
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmLoadingOverlay
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.mappers.toMessage

@Composable
fun FavoritesDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: FavoritesDetailViewModel = hiltViewModel<FavoritesDetailViewModel>()
) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onStart()
    }

    BackHandler {
        onBack()
    }

    FavoritesDetailContent(
        modifier = modifier,
        state = state,
        onRetry = { viewModel.onStart() }
    )

}

@Composable
fun FavoritesDetailContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesDetailViewModel.UiState>,
    onRetry: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val content = state.value.content) {
            is FavoritesDetailViewModel.DetailContent.Loading -> {
                MmLoadingOverlay()
            }

            is FavoritesDetailViewModel.DetailContent.Error -> {
                MmErrorState(
                    message = content.error.toMessage(),
                    onRetry = onRetry
                )
            }

            is FavoritesDetailViewModel.DetailContent.Success -> {
                FavoritesDetailInfo(cityInfo = content.city)
            }
        }
    }
}

@Composable
private fun FavoritesDetailInfo(
    cityInfo: CityWeatherDomain?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MeteoMartoTheme.spacing.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MmText.DisplayLarge(
            text = cityInfo?.name ?: stringResource(R.string.weather_not_available),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cityInfo?.weatherIcon).crossfade(true).build(),
            contentDescription = stringResource(R.string.weather_icon_description),
            modifier = Modifier
                .height(dimensionResource(R.dimen.weather_icon_size))
                .width(dimensionResource(R.dimen.weather_icon_size)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.small))
        cityInfo?.weatherDescription?.let { weatherDescription ->
            MmText.HeadlineMedium(text = weatherDescription)
        }

        Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

        MmText.BodyLarge(
            text = stringResource(R.string.city_current_temperature, cityInfo?.temperature.toString())
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.small))
        MmText.BodyLarge(
            text = stringResource(R.string.city_max_temperature, cityInfo?.temperatureMax.toString()),
            color = MeteoMartoTheme.colors.error
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.small))
        MmText.BodyLarge(
            text = stringResource(R.string.city_min_temperature, cityInfo?.temperatureMin.toString()),
            color = MeteoMartoTheme.colors.primary // Using primary for cold as fallback if no blue token
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.small))
        MmText.BodyLarge(
            text = stringResource(R.string.city_pressure, cityInfo?.pressure.toString())
        )
        Spacer(Modifier.height(MeteoMartoTheme.spacing.small))
        MmText.BodyLarge(
            text = stringResource(R.string.city_rain, cityInfo?.rain.toString())
        )
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun FavoritesDetailScreenPreview() {
    val dummyCity = CityWeatherDomain(
        name = "Sabadell",
        temperature = 25.5,
        temperatureMin = 20.0,
        temperatureMax = 30.0,
        pressure = 1012,
        weatherDescription = "Partly Cloudy"
    )
    val dummyState = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(
            FavoritesDetailViewModel.UiState(
                content = FavoritesDetailViewModel.DetailContent.Success(dummyCity)
            )
        )
    }

    MeteoMartoTheme {
        FavoritesDetailContent(
            state = dummyState,
            onRetry = { }
        )
    }
}
