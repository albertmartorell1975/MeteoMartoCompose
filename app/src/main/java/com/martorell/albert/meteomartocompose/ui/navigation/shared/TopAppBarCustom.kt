package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(scrollBehavior: TopAppBarScrollBehavior? = null) {

    //TopAppBar(
    LargeTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {

            AppBarAction(
                imageVector = Icons.Default.Search,
                onClick = {/*TODO*/ })
            AppBarAction(
                imageVector = Icons.Default.Share,
                onClick = {/*TODO*/ })

        },
        navigationIcon = {
            AppBarAction(
                imageVector = Icons.Default.Menu,
                onClick = {/*TODO*/ })
        },
        scrollBehavior = scrollBehavior
    )

}

@Composable
private fun AppBarAction(imageVector: ImageVector, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}