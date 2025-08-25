package com.martorell.albert.meteomartocompose.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    goToLogin: () -> Unit,
    goToDashboard: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>()
) {

    val state = viewModel.state.collectAsState()
    SplashContent(
        goToLogin = { goToLogin() },
        goToDashboard = { goToDashboard() },
        state = state
    )

}

@Composable
fun SplashContent(
    goToLogin: () -> Unit,
    goToDashboard: () -> Unit,
    state: State<SplashViewModel.UiState>
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash),
            contentDescription = null, modifier = Modifier.size(150.dp, 150.dp)
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.standard_height)))
        Text(
            stringResource(R.string.welcome),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

    }

    // Executed only once
    LaunchedEffect(key1 = true) {
        delay(3000)
        if (state.value.userLogged)
            goToDashboard()
        else
            goToLogin()
    }

}

@Preview(showBackground = true)
@Composable
fun SplashContentPreview() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash),
            contentDescription = null, modifier = Modifier.size(150.dp, 150.dp)
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.standard_height)))
        Text(
            stringResource(R.string.welcome),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }

    LaunchedEffect(key1 = true) {
        delay(3000)
    }

}