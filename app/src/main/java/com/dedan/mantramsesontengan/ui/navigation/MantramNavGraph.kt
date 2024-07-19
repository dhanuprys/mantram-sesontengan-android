package com.dedan.mantramsesontengan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dedan.mantramsesontengan.ui.screen.mantramdetail.MantramDetailDestination
import com.dedan.mantramsesontengan.ui.screen.mantramdetail.MantramDetailScreen
import com.dedan.mantramsesontengan.ui.screen.mantramselectbase.MantramSelectBaseDestination
import com.dedan.mantramsesontengan.ui.screen.mantramselectbase.MantramSelectBaseScreen
import com.dedan.mantramsesontengan.ui.screen.mantramselectsub.MantramSelectSubDestination
import com.dedan.mantramsesontengan.ui.screen.mantramselectsub.MantramSelectSubScreen

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
            MantramSelectBaseScreen(
                onMantramSelect = {
                    navController.navigate("${MantramSelectSubDestination.route}/${it.id}")
                }
            )
        }
        composable(
            route = MantramSelectSubDestination.routeWithArgs,
            arguments = listOf(
                navArgument(
                    name = MantramSelectSubDestination.mantramBaseIdArg,
                    builder = { type = NavType.IntType }
                )
            )
        ) {
            MantramSelectSubScreen(
                onMantramSubSelect = { mantramSubType, mantramBaseId ->
                    navController.navigate("${MantramDetailDestination.route}/${mantramBaseId}/${mantramSubType.id}")
                }
            )
        }

        composable(
            route = MantramDetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(
                    name = MantramDetailDestination.mantramBaseIdArg,
                    builder = { type = NavType.IntType }
                ),
                navArgument(
                    name = MantramDetailDestination.mantramIdArg,
                    builder = { type = NavType.IntType }
                )
            )
        ) {
            MantramDetailScreen()
        }
    }
}