package com.dedan.mantramsesontengan.ui.screen.mantramselectbase

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination

object MantramSelectBaseDestination : NavigationDestination {
    override val route = "mantram_select_base"
    override val titleRes = R.string.app_name
}

@Composable
fun MantramSelectBaseScreen(
    modifier: Modifier = Modifier,
    viewModel: MantramSelectBaseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(modifier = modifier) { innerPadding ->

    }
}

@Composable
fun MantramSelectBaseBody() {

}