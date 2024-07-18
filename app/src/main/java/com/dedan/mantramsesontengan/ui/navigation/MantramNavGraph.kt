package com.dedan.mantramsesontengan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dedan.mantramsesontengan.ui.screen.mantramselectbase.MantramSelectBaseDestination
import com.dedan.mantramsesontengan.ui.screen.mantramselectbase.MantramSelectBaseScreen

@Composable
fun MantramNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MantramSelectBaseDestination.route,
        modifier = modifier
    ) {
        composable(route = MantramSelectBaseDestination.route) {
            MantramSelectBaseScreen()
        }
    }
}