package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.R
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    markCityAsFavorite: KFunction0<Unit>
) {

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {

            AppBarAction(
                imageVector = Icons.Default.FavoriteBorder,
                onClickAction = markCityAsFavorite
            )
        },
        scrollBehavior = scrollBehavior
    )

}

@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onClickAction: KFunction0<Unit>
) {

    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            coroutineScope.launch { onClickAction() }
        }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }

}