package com.dedan.mantramsesontengan.ui.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.dedan.mantramsesontengan.ui.screen.savedmantram.SavedMantramDestination
import com.dedan.mantramsesontengan.ui.screen.savedmantram.SavedMantramScreen

@Composable
fun MantramNavHost(
    globalViewModel: GlobalViewModel,
    onDrawerOpenRequest: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val audioPlayerUiState = globalViewModel.audioPlayerUiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = MantramSelectBaseDestination.route,
        modifier = modifier
    ) {
        composable(
            route = MantramSelectBaseDestination.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MantramSelectBaseScreen(
                globalViewModel = globalViewModel,
                onDrawerOpenRequest = onDrawerOpenRequest,
                onMantramSelect = {
                    navController.navigate("${MantramSelectSubDestination.route}/${it.id}")
                },
                navigateToSavedMantram = { navController.navigate(SavedMantramDestination.route) }
            )
        }
        composable(
            route = MantramSelectSubDestination.routeWithArgs,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            arguments = listOf(
                navArgument(
                    name = MantramSelectSubDestination.mantramBaseIdArg,
                    builder = { type = NavType.IntType }
                )
            )
        ) {
            MantramSelectSubScreen(
                globalViewModel = globalViewModel,
                onMantramSubSelect = { mantramSubType, mantramBaseId ->
                    navController.navigate("${MantramDetailDestination.route}/${mantramBaseId}/${mantramSubType.id}")
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = MantramDetailDestination.routeWithArgs,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
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
            MantramDetailScreen(
                globalViewModel = globalViewModel,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = SavedMantramDestination.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SavedMantramScreen(
                globalViewModel = globalViewModel,
                navigateToSavedMantramDetail = {},
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}