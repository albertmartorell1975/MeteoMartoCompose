package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    goToDetail: () -> Unit,
    onTestSharedViewModel: KSuspendFunction0<Boolean>
) {

    val coroutineScope = rememberCoroutineScope()

    MeteoMartoComposeLayout {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(count = 100) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "És la current la city? $it",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                shadow = Shadow(
                                    offset = Offset(
                                        x = 5f, y = 5f
                                    ), blurRadius = 6f, color = Color.Black.copy(alpha = 0.5f)
                                )
                            ),
                            modifier = Modifier
                                .clickable { coroutineScope.launch { onTestSharedViewModel.invoke() } }
                                .background(Color.Cyan)
                                .border(
                                    width = dimensionResource(R.dimen.padding_small),
                                    color = Color.Blue
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

        /*Column(
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
                    text = "És la current la city?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            offset = Offset(
                                x = 5f, y = 5f
                            ), blurRadius = 6f, color = Color.Black.copy(alpha = 0.5f)
                        )
                    ),
                    modifier = Modifier
                        .clickable { coroutineScope.launch { onTestSharedViewModel.invoke() } }
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

            Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

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
                        .clickable { goToDetail() }
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

         */

    }


}