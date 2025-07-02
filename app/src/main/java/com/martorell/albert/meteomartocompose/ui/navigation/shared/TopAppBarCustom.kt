package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        scrollBehavior = scrollBehavior
        /*
        actions = {

            AppBarAction(
                imageVector = Icons.Default.FavoriteBorder,
                onClickAction = markCityAsFavorite
            )
        },
         */
    )

}

/*

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
 */