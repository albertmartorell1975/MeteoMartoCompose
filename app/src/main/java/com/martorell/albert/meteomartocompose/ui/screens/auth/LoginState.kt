package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


@Composable
fun rememberLoginUIState(
    userText: String = "",
): LoginUIState = remember(userText) {
    LoginUIState(userText)
}

class LoginUIState(var userText: String = "") {


    // private val _userState by rememberSaveable { ""}
    //val userState = _userState.asStateFlow()


    fun updateUser(newUser: String) {

        userText = newUser
        //Log.i("NavigationBar", "The current destination is  $screen")
        //val navBackStackEntry = navController.currentBackStackEntryAsState().value
        //val currentDestination = navBackStackEntry?.destination
        //return currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true

    }

    /*fun updateUser(user: String) {
        // Do validation stuff
        userText = user
    }

     */

}

/*class LoginState() {

    private val _userState by rememberSaveable { mutableStateOf("") }
    val userState = _userState.asStateFlow()

    fun setText(userText:String) {

        _userState.value = userText
    }


}
 */

/*
@Composable
fun rememberLoginState(
    navController: NavHostController = rememberNavController(),
): LoginState = remember(navController) {
    LoginState(navController)
}

class LoginState(
    val navController: NavHostController,
) {




}

 */