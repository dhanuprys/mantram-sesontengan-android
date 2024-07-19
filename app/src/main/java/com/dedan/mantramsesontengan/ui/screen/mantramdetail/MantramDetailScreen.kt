package com.dedan.mantramsesontengan.ui.screen.mantramdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.layout.PageError
import com.dedan.mantramsesontengan.ui.layout.PageLoading
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination

object MantramDetailDestination : NavigationDestination {
    override val route = "mantram_detail"
    override val titleRes = R.string.app_name

    const val mantramBaseIdArg = "mantramBaseId"
    const val mantramIdArg = "mantramId"
    val routeWithArgs = "$route/{$mantramBaseIdArg}/{$mantramIdArg}"
}

@Composable
fun MantramDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MantramDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(modifier = modifier) { innerPadding ->
        MantramDetailBody(
            mantramDetailUiState = viewModel.mantramDetailUiState
        )
    }
}

@Composable
fun MantramDetailBody(
    mantramDetailUiState: MantramDetailUiState,
    modifier: Modifier = Modifier
) {
    when (mantramDetailUiState) {
        is MantramDetailUiState.Error -> PageError(modifier = modifier)
        is MantramDetailUiState.Loading -> PageLoading(modifier = modifier)
        is MantramDetailUiState.Success -> {}
    }
}