package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.LocalAsyncImagePreviewHandler
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmErrorState
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmLoadingOverlay
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmScaffold
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmWeatherInfo
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.mappers.toMessage
import com.martorell.albert.meteomartocompose.utils.previewAsyncImageCoil

@Composable
fun FavoritesDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: FavoritesDetailViewModel = hiltViewModel<FavoritesDetailViewModel>(),
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
    ) {
        viewModel.onStart()
    }

}

@Composable
fun FavoritesDetailContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesDetailViewModel.UiState>,
    onRetry: () -> Unit
) {
    MmScaffold(modifier = modifier) {
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
                content.city?.let { city ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(MeteoMartoTheme.spacing.medium),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            MmWeatherInfo(
                                city = city,
                                isHighTempAlertActive = false, // TODO: Enrich city domain with this info
                                modifier = Modifier.fillMaxWidth(),
                                titleFontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
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
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewAsyncImageCoil) {
            FavoritesDetailContent(
                state = dummyState,
                onRetry = { }
            )
        }
    }
}
