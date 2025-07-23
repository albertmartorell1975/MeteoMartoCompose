package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun FavoriteItem(
    modifier: Modifier = Modifier,
    clickOnRow: () -> Unit,
    clickOnDelete: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clickable { clickOnRow() }) {

        // Create references for the composable to constrain
        val (deleteIcon, cityName, weather) = createRefs()

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
                .clickable { clickOnDelete() },
            tint = Color.Black
        )

        Text(
            color = Color.DarkGray,
            modifier = Modifier.constrainAs(weather) {
                top.linkTo(cityName.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 16.dp)
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

@Composable
@Preview(showBackground = true, widthDp = 600, heightDp = 200)
fun FavoriteItemPreview(
    modifier: Modifier = Modifier,
) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        // Create references for the composables to constrain
        val (deleteIcon, cityName, weather) = createRefs()

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
                .clickable { },
            tint = Color.Red
        )

        Text(
            color = Color.DarkGray,
            modifier = Modifier.constrainAs(weather) {
                top.linkTo(cityName.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 16.dp)
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