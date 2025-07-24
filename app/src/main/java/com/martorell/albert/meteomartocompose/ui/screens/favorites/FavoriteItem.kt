package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.utils.previewAsyncImageCoil

@Composable
fun FavoriteItem(
    modifier: Modifier = Modifier,
    city: CityWeatherDomain,
    clickOnRow: () -> Unit,
    clickOnDelete: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clickable { clickOnRow() }) {

        // Create references for the composable to constrain
        val (deleteIcon, cityName, weather, weatherIcon) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(cityName) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(deleteIcon.start, margin = 16.dp)
                    bottom.linkTo(weatherIcon.top, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            text = city.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(deleteIcon) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .clickable { clickOnDelete() },
            tint = Color.Black
        )

        AsyncImage(
            modifier = Modifier
                .height(dimensionResource(R.dimen.weather_icon_size))
                .width(
                    dimensionResource(R.dimen.weather_icon_size)
                )
                .constrainAs(weatherIcon) {
                    top.linkTo(cityName.bottom, margin = 8.dp)
                    end.linkTo(weather.start, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(city.weatherIcon).crossfade(true).build(),
            contentDescription = city.weatherDescription,
            contentScale = ContentScale.Crop
        )

        city.weatherDescription?.also {
            Text(
                color = Color.DarkGray,
                modifier = Modifier.constrainAs(weather) {
                    top.linkTo(cityName.bottom, margin = 8.dp)
                    bottom.linkTo(weatherIcon.bottom)
                    start.linkTo(weatherIcon.end)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
                text = it,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        } ?: run {

            Text(
                color = Color.DarkGray,
                modifier = Modifier.constrainAs(weather) {
                    top.linkTo(cityName.bottom, margin = 8.dp)
                    bottom.linkTo(weatherIcon.bottom)
                    start.linkTo(weatherIcon.end)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(R.string.weather_not_available),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        }


    }

}



@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview(showBackground = true, widthDp = 600, heightDp = 200)
fun FavoriteItemPreview(
    modifier: Modifier = Modifier,
) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        // Create references for the composables to constrain
        val (deleteIcon, cityName, weather, weatherIcon) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(cityName) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(deleteIcon.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            text = "Sabadell fadasfasfasfasdfas afadsfsfas fasd fa sf as fas fasf asd fa f a",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(deleteIcon) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .clickable {},
            tint = Color.Black
        )

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewAsyncImageCoil) {
            AsyncImage(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.weather_icon_size))
                    .width(
                        dimensionResource(R.dimen.weather_icon_size)
                    )
                    .constrainAs(weatherIcon) {
                        top.linkTo(cityName.bottom, margin = 8.dp)
                        end.linkTo(weather.start, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                model = "https://example.com/image.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Text(
            color = Color.DarkGray,
            modifier = Modifier.constrainAs(weather) {
                top.linkTo(cityName.bottom, margin = 8.dp)
                start.linkTo(weatherIcon.end, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            text = "Definició del temps que hi ha previst per la viutat que hem carregat a " +
                    "la vila",
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

    }

}