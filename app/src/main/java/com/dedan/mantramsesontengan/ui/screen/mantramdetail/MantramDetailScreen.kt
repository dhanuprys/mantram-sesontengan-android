package com.dedan.mantramsesontengan.ui.screen.mantramdetail

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.AudioBottomBar
import com.dedan.mantramsesontengan.MantramAppBar
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.model.MantramDetail
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.layout.PageError
import com.dedan.mantramsesontengan.ui.layout.PageLoading
import com.dedan.mantramsesontengan.ui.navigation.AudioPlayerUiState
import com.dedan.mantramsesontengan.ui.navigation.GlobalViewModel
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme

object MantramDetailDestination : NavigationDestination {
    override val route = "mantram_detail"
    override val titleRes = R.string.app_name

    const val mantramBaseIdArg = "mantramBaseId"
    const val mantramIdArg = "mantramId"
    val routeWithArgs = "$route/{$mantramBaseIdArg}/{$mantramIdArg}"
}

@Composable
fun MantramDetailScreen(
    navigateUp: () -> Unit,
    globalViewModel: GlobalViewModel,
    modifier: Modifier = Modifier,
    viewModel: MantramDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val audioPlayerUiState = globalViewModel.audioPlayerUiState.collectAsState()

    LaunchedEffect(viewModel.mantramDetailUiState) {
        if (viewModel.mantramDetailUiState !is MantramDetailUiState.Success) {
            return@LaunchedEffect
        }

        (viewModel.mantramDetailUiState as MantramDetailUiState.Success).data.apply {
            if (audioUrl != null) {
                globalViewModel.prepareAudio(audioUrl)
            }
        }
    }

    Scaffold(
        topBar = {
            MantramAppBar(
                canNavigateBack = true,
                onNavigateUp = navigateUp
            )
        },
        bottomBar = {
            AudioBottomBar(
                audioPlayerUiState = audioPlayerUiState.value,
                onPlayRequest = { globalViewModel.playAudio() },
                onStopRequest = { globalViewModel.stopAudio() },
                onRestartRequest = { globalViewModel.restartAudio() }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        MantramDetailBody(
            mantramDetailUiState = viewModel.mantramDetailUiState,
            modifier = Modifier.padding(innerPadding)
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
        is MantramDetailUiState.Success -> MantramWrapper(
            mantramDetail = mantramDetailUiState.data,
            modifier = modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramDetailBodyPreview_Success() {
    MantramSesontenganTheme {
        MantramDetailBody(
            mantramDetailUiState = MantramDetailUiState.Success(
                MantramDetail(
                    id = 1,
                    name = "Test",
                    mantram = "Test",
                    description = "Test",
                    audioUrl = "Test",
                    version = 0,
                    updatedAt = "Test"
                )
            )
        )
    }
}

@Composable
fun MantramWrapper(
    mantramDetail: MantramDetail,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Column(modifier = modifier) {
            MantramContent(
                text = mantramDetail.mantram,
                modifier = Modifier.heightIn(min = 250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            MantramDescription(text = mantramDetail.description)
        }
    }
}

@Composable
fun MantramContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Mantram")
        Text(text)
    }
}

@Preview
@Composable
fun MantramContentPreview() {
    MantramSesontenganTheme {
        MantramContent(text = "Hello")
    }
}

@Composable
fun MantramDescription(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Description")
        Text(text)
    }
}

@Preview
@Composable
fun MantramDescriptionPreview() {
    MantramSesontenganTheme {
        MantramDescription("Text")
    }
}