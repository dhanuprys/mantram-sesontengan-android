package com.dedan.mantramsesontengan.ui.screen.mantramdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.model.MantramDetail
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.layout.PageError
import com.dedan.mantramsesontengan.ui.layout.PageLoading
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
    modifier: Modifier = Modifier,
    viewModel: MantramDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(modifier = modifier) { innerPadding ->
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
                    version = "Test",
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
    Column(modifier = modifier) {
        MantramContent(
            text = mantramDetail.name,
            modifier = Modifier.heightIn(min = 250.dp)
        )
        MantramDescription(text = mantramDetail.description)
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