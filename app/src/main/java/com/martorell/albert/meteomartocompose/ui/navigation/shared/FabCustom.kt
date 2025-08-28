package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.R


@Composable
fun FabCustom(
    onClicked: () -> Unit,
    icon: ImageVector?

) {

    FloatingActionButton(onClick = onClicked) {
        icon?.run {
            Icon(
                imageVector = this,
                contentDescription = stringResource(R.string.favorite)
            )
        }
    }

}