package com.martorell.albert.meteomartocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.martorell.albert.meteomartocompose.ui.MainViewModel
import com.martorell.albert.meteomartocompose.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Skip the artificial splash delay if the activity was started with deep link data
        val isDeepLink = intent?.data != null
        viewModel.checkAuthStatus(skipDelay = isDeepLink)

        enableEdgeToEdge()
        setContent {
            Navigation(mainViewModel = viewModel)
        }
    }
}
