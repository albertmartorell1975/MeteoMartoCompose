package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.screens.shared.CityTextView
import com.martorell.albert.meteomartocompose.ui.screens.shared.ErrorScreen
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun FavoritesDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FavoritesDetailViewModel = hiltViewModel<FavoritesDetailViewModel>()
) {

    val state = viewModel.state.collectAsState()
    FavoritesDetailContent(
        modifier = modifier,
        state = state,
        loadCityWeatherAction = viewModel::loadCityWeather,
        backHandlerAction = { navController.popBackStack() }
    )

}

@Composable
fun FavoritesDetailContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesDetailViewModel.UiState>,
    loadCityWeatherAction: KSuspendFunction0<Unit>,
    backHandlerAction: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        state.value.city.fold({
            // the error to display
            ErrorScreen(
                customError = it,
                setTryAgainState = {
                    coroutineScope.launch {
                        loadCityWeatherAction()
                    }
                },
                onBackHandlerAction = backHandlerAction
            )

        }
        )
        { cityInfo ->

            CityTextView(
                contentFix = cityInfo?.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cityInfo?.weatherIcon).crossfade(true).build(),
                contentDescription = "El temps que fa",
                modifier = Modifier
                    .height(dimensionResource(R.dimen.weather_icon_size))
                    .width(
                        dimensionResource(R.dimen.weather_icon_size)
                    ),
                contentScale = ContentScale.Crop
            )

            cityInfo?.weatherDescription?.let { weatherDescription ->

                CityTextView(
                    contentFix = weatherDescription,
                    fontWeight = FontWeight.Bold
                )
            }

            CityTextView(
                contentFix = stringResource(R.string.city_current_temperature),
                contentDynamic = cityInfo?.temperature.toString()
            )

            CityTextView(
                contentFix = stringResource(R.string.city_max_temperature),
                contentDynamic = cityInfo?.temperatureMax.toString(),
                colorDynamic = Color.Red
            )

            CityTextView(
                contentFix = stringResource(R.string.city_min_temperature),
                contentDynamic = cityInfo?.temperatureMin.toString(),
                colorDynamic = Color.Blue
            )

            CityTextView(
                contentFix = stringResource(R.string.city_pressure),
                contentDynamic = cityInfo?.pressure.toString()
            )

            CityTextView(
                contentFix = stringResource(R.string.city_rain),
                contentDynamic = cityInfo?.rain.toString(),
                colorDynamic = Color.Blue
            )
        }

    }

}