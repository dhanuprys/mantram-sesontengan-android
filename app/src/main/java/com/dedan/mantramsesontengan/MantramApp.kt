package com.dedan.mantramsesontengan

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedan.mantramsesontengan.ui.navigation.MantramNavHost

@Composable
fun MantramApp(
    navController: NavHostController = rememberNavController()
) {
    MantramNavHost(navController)
}