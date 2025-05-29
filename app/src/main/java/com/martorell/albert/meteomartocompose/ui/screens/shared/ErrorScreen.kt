package com.martorell.albert.meteomartocompose.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.martorell.albert.meteomartocompose.data.CustomError

@Composable
fun ErrorScreen(customError: CustomError) {

    val message = when (customError) {
        CustomError.Connectivity -> "Connectivity error"
        is CustomError.FirebaseError -> "Firebase error"
        is CustomError.Unknown -> "CustomError error: ${customError.message}"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = message,
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

    }

}