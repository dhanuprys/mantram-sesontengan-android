package com.dedan.mantramsesontengan.ui.screen.mantramdetail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.dedan.mantramsesontengan.ui.theme.TypographySize
import kotlinx.coroutines.launch

object MantramDetailDestination : NavigationDestination {
    override val route = "mantram_detail"
    override val titleRes = R.string.app_name

    const val mantramBaseIdArg = "mantramBaseId"
    const val mantramIdArg = "mantramId"
    const val offlineModeArg = "offlineMode"

    val routeWithArgs =
        "$route/{$mantramBaseIdArg}/{$mantramIdArg}?$offlineModeArg={$offlineModeArg}"
}

@Composable
fun MantramDetailScreen(
    navigateUp: () -> Unit,
    globalViewModel: GlobalViewModel,
    modifier: Modifier = Modifier,
    viewModel: MantramDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val audioPlayerUiState = globalViewModel.audioPlayerUiState.collectAsState()
    val typographySize by globalViewModel.typographySize.collectAsState()
    var bottomLoading by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.mantramDetailUiState) {
        if (viewModel.mantramDetailUiState !is MantramDetailUiState.Success) {
            return@LaunchedEffect
        }

        (viewModel.mantramDetailUiState as MantramDetailUiState.Success).data.apply {
            bottomLoading = false

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
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Text("Detail Mantram")
                    }

                    if (viewModel.offlineMode) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.removeFromBookmark()
                                    globalViewModel.showToast("Mantram berhasil dihapus")
                                    navigateUp()
                                }
                            }
                        ) {
                            Text("Hapus")
                        }
                    } else if (viewModel.mantramSavedStatusUiState !is MantramSavedStatusUiState.Unknown) {
                        val isMantramSaved =
                            viewModel.mantramSavedStatusUiState is MantramSavedStatusUiState.Saved
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (isMantramSaved) {
                                        viewModel.removeFromBookmark()
                                    } else {
                                        viewModel.storeInBookmark()
                                    }

                                    globalViewModel.showToast(
                                        if (isMantramSaved) "Mantram berhasil dihapus" else "Mantram berhasil disimpan"
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter =
                                when (viewModel.mantramSavedStatusUiState) {
                                    is MantramSavedStatusUiState.Saved -> painterResource(id = R.drawable.ic_bookmark)
                                    is MantramSavedStatusUiState.Unknown,
                                    is MantramSavedStatusUiState.NotSaved -> painterResource(id = R.drawable.ic_bookmark_border)

                                    is MantramSavedStatusUiState.NeedUpdate -> painterResource(id = R.drawable.ic_update)
                                },
                                contentDescription = null
                            )
                        }

                        IconButton(onClick = {
                            shareOrder(
                                context,
                                (viewModel.mantramDetailUiState as MantramDetailUiState.Success).data.name,
                                (viewModel.mantramDetailUiState as MantramDetailUiState.Success).data.mantram
                            )
                        }) {
                            Icon(imageVector = Icons.Filled.Share, contentDescription = null)
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (viewModel.offlineMode) {
                OfflineBottomBar()
                return@Scaffold
            }

            if (!bottomLoading) {
                AudioBottomBar(
                    audioPlayerUiState = audioPlayerUiState.value,
                    onPlayRequest = { globalViewModel.playAudio() },
                    onStopRequest = { globalViewModel.stopAudio() },
                    onRestartRequest = { globalViewModel.restartAudio() }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FontSizeSelector(
                currentFontSize = typographySize,
                onFontChange = {
                    coroutineScope.launch {
                        globalViewModel.changeTypography(it)
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
            MantramDetailBody(
                mantramDetailUiState = viewModel.mantramDetailUiState
            )
        }
    }
}

@Composable
fun FontSizeItem(
    onClick: () -> Unit,
    fontSize: TextUnit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selected) Color.Unspecified
                else Color.Transparent,
            contentColor =
                if (selected) Color.Unspecified
                else MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(2.dp),
        modifier = modifier
    ) {
        Text("A", fontSize = fontSize)
    }
}

@Composable
fun FontSizeSelector(
    currentFontSize: TypographySize = TypographySize.NORMAL,
    onFontChange: (TypographySize) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Ukuran font")
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            FontSizeItem(
                onClick = {
                    onFontChange(TypographySize.SMALL)
                },
                selected = currentFontSize == TypographySize.SMALL,
                fontSize = 12.sp,
            )
            FontSizeItem(
                onClick = {
                    onFontChange(TypographySize.NORMAL)
                },
                selected = currentFontSize == TypographySize.NORMAL,
                fontSize = 16.sp
            )
            FontSizeItem(
                onClick = {
                    onFontChange(TypographySize.LARGE)
                },
                selected = currentFontSize == TypographySize.LARGE,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FontSizeSelectorPreview() {
    MantramSesontenganTheme {
        FontSizeSelector()
    }
}

@Composable
fun MantramDetailBody(
    mantramDetailUiState: MantramDetailUiState,
    modifier: Modifier = Modifier
) {
    when (mantramDetailUiState) {
        is MantramDetailUiState.Error -> PageError(modifier = modifier.fillMaxSize())
        is MantramDetailUiState.Loading -> PageLoading(modifier = modifier.fillMaxSize())
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
        Column {
            FontSizeSelector()
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
}

@Composable
fun MantramWrapper(
    mantramDetail: MantramDetail,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())) {
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
        Text(
            text = "Mantram",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = text,
            fontStyle = FontStyle.Italic
        )
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
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )
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

@Composable
fun OfflineBottomBar(modifier: Modifier = Modifier) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = "Mode offline",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun OfflineBottomBarPreview() {
    MantramSesontenganTheme {
        Scaffold(
            bottomBar = { OfflineBottomBar() }
        ) { innerPadding -> innerPadding }
    }
}

private fun shareOrder(context: Context, subject: String, summary: String) {
    val formattedMessage = subject + "\n" + summary
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
//        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, formattedMessage)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            "Share using"
        )
    )
}
