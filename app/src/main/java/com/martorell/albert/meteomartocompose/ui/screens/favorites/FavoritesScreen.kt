package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.screens.shared.AlertDialogCustom
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction0

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    goToDetail: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>()
) {

    val state = viewModel.state.collectAsState()

    FavoriteContent(
        modifier = modifier,
        state = state,
        goToDetail = { goToDetail() },
        displayAlertDialogAction = viewModel::userClickedOnDeleteFavoriteCity,
        dismissAlertDialogAction = viewModel::userDismissedAlertDialog,
        removeCityFromFavoritesAction = viewModel::removeCityFromFavorites
    )

}


@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesViewModel.UiState>,
    goToDetail: () -> Unit,
    displayAlertDialogAction: KFunction1<String, Unit>,
    dismissAlertDialogAction: () -> Unit,
    removeCityFromFavoritesAction: KSuspendFunction0<Unit>
) {

    val coroutineScope = rememberCoroutineScope()

    if (state.value.citiesFavorites.isEmpty()) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(24.dp)
                    // make text center vertical
                    .wrapContentHeight()
                // align bottom
                //.wrapContentHeight(Alignment.Bottom)
                ,
                text = stringResource(R.string.no_favorites_cities),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

        }

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items(count = state.value.citiesFavorites.size) { index ->
                FavoriteItem(
                    city = state.value.citiesFavorites[index],
                    clickOnDelete = {
                        displayAlertDialogAction(state.value.citiesFavorites[index].name)
                    },
                    clickOnRow = goToDetail
                )
            }
        }

        if (state.value.cityToUnMarkAsFavorite.isNotEmpty())
            AlertDialogCustom(
                title = R.string.delete_favority_city_title,
                content = R.string.delete_favority_city_explanation,
                actionText = R.string.delete_favority_city_action,
                dismissText = R.string.delete_favority_city_cancel,
                onDismissAction = dismissAlertDialogAction,
                onConfirmAction = {
                    coroutineScope.launch {
                        removeCityFromFavoritesAction()
                    }
                })

    }

}