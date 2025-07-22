package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.dimensionResource
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    MeteoMartoComposeLayout {

        Scaffold(
        ) { innerPadding ->

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Anar al detall",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = Shadow(
                                offset = Offset(
                                    x = 5f, y = 5f
                                ), blurRadius = 6f, color = Color.Black.copy(alpha = 0.5f)
                            )
                        ),

                        modifier = Modifier
                            .clickable { onClick() }
                            .background(Color.Cyan)
                            .border(
                                width = dimensionResource(R.dimen.padding_small), color = Color.Blue
                            )
                            .padding(
                                horizontal = dimensionResource(R.dimen.padding_small),
                                vertical = dimensionResource(R.dimen.padding_small)
                            )

                    )
                }

            }

        }
    }

}