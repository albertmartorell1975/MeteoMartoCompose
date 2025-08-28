package com.martorell.albert.meteomartocompose.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import com.martorell.albert.meteomartocompose.MeteoMartoApp
import java.math.RoundingMode

val Context.MeteoMartoApp: MeteoMartoApp
    get() = applicationContext as MeteoMartoApp

fun Double.fahrenheitToCelsius(): Double {
    return (this - 32) * 5 / 9
}

fun Double.openWeatherConverter(): Double {
    return (this.div(10).toBigDecimal().setScale(
        1, RoundingMode.HALF_UP
    )).toDouble()
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController, navGraphRoute: String, navBackStackEntry: NavBackStackEntry
): T {
    val parentEntry  = remember(navBackStackEntry){
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

/**
 * The Android Studio preview behaviour for AsyncImage/rememberAsyncImagePainter/SubcomposeAsyncImage is controlled by the LocalAsyncImagePreviewHandler.
 * By default, it will attempt to perform the request as normal inside the preview environment.
 * Network access is disabled in the preview environment so network URLs will always fail.
 *
 * We can override the preview behaviour like so:
 *
 * @OptIn(ExperimentalCoilApi::class)
 * val previewAsyncImageCoil = AsyncImagePreviewHandler {
 *     ColorImage(Color.Red.toArgb())
 * }
 *
 * And then create a composition local provider into the preview. This Composition Local provider, in turn provides the previewAsyncImageCoil.
 * For example:
 *
 *
 * @OptIn(ExperimentalCoilApi::class)
 * @Composable
 * @Preview(showBackground = true, widthDp = 600, heightDp = 200)
 * fun FavoriteItemPreview(){
 *
 * ......
 *
 * CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewAsyncImageCoil) {
 *     AsyncImage(
 *         modifier = Modifier
 *             .height(dimensionResource(R.dimen.weather_icon_size))
 *             .width(
 *                 dimensionResource(R.dimen.weather_icon_size)
 *             )
 *             .constrainAs(weatherIcon) {
 *                 top.linkTo(cityName.bottom, margin = 8.dp)
 *                 end.linkTo(weather.start, margin = 16.dp)
 *                 start.linkTo(parent.start, margin = 16.dp)
 *             },
 *         model = "https://example.com/image.jpg",
 *         contentDescription = null,
 *         contentScale = ContentScale.Crop
 *     )
 * }
 *
 * .....
 *
 * }
 *
 */
@OptIn(ExperimentalCoilApi::class)
val previewAsyncImageCoil = AsyncImagePreviewHandler {
    ColorImage(Color.Green.toArgb())
}