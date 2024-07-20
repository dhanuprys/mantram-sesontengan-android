package com.dedan.mantramsesontengan.ui.screen.mantramselectsub

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.AudioBottomBar
import com.dedan.mantramsesontengan.MantramAppBar
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.model.MantramSubType
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.layout.PageError
import com.dedan.mantramsesontengan.ui.layout.PageLoading
import com.dedan.mantramsesontengan.ui.navigation.AudioPlayerUiState
import com.dedan.mantramsesontengan.ui.navigation.GlobalViewModel
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme

object MantramSelectSubDestination : NavigationDestination {
    override val route = "mantram_select_sub"
    override val titleRes = R.string.app_name

    val mantramBaseIdArg = "mantramBaseId"
    val routeWithArgs = "$route/{$mantramBaseIdArg}"
}

@Composable
fun MantramSelectSubScreen(
    globalViewModel: GlobalViewModel,
    onMantramSubSelect: (MantramSubType, Int) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MantramSelectSubViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val audioPlayerUiState = globalViewModel.audioPlayerUiState.collectAsState()

    Scaffold(
        topBar = {
            MantramAppBar(
                canNavigateBack = true,
                onNavigateUp = navigateUp
            ) {
                Text("Pilih Mantram")
            }
        },
        bottomBar = {
            if (audioPlayerUiState.value is AudioPlayerUiState.Playing) {
                AudioBottomBar(
                    audioPlayerUiState = audioPlayerUiState.value,
                    onPlayRequest = {},
                    onStopRequest = { globalViewModel.stopAudio() },
                    onRestartRequest = { globalViewModel.restartAudio() }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        MantramSelectSubBody(
            mantramSubTypesUiState = viewModel.mantramSubTypesUiState,
            onMantramSubSelect = {
                onMantramSubSelect(it, viewModel.mantramBaseId)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MantramSelectSubBody(
    mantramSubTypesUiState: MantramSubTypesUiState,
    onMantramSubSelect: (MantramSubType) -> Unit,
    modifier: Modifier = Modifier
) {
    when (mantramSubTypesUiState) {
        is MantramSubTypesUiState.Error -> PageError(modifier = modifier.fillMaxSize())
        is MantramSubTypesUiState.Loading -> PageLoading(modifier = modifier.fillMaxSize())
        is MantramSubTypesUiState.Success -> MantramSubTypesList(
            mantramSubTypes = mantramSubTypesUiState.data,
            onSelect = onMantramSubSelect,
            modifier = modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectSubBodyPreview_Loading() {
    MantramSesontenganTheme {
        MantramSelectSubBody(
            mantramSubTypesUiState = MantramSubTypesUiState.Loading,
            onMantramSubSelect = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectSubBodyPreview_Error() {
    MantramSesontenganTheme {
        MantramSelectSubBody(
            mantramSubTypesUiState = MantramSubTypesUiState.Error,
            onMantramSubSelect = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectSubBody_Success() {
    MantramSesontenganTheme {
        MantramSelectSubBody(
            mantramSubTypesUiState = MantramSubTypesUiState.Success(
                listOf(
                    MantramSubType(1, "Test", "Test", "Test"),
                    MantramSubType(2, "Test2", "Test2", "Test2"),
                    MantramSubType(3, "Test3", "Test3", "Test3")
                )
            ),
            onMantramSubSelect = {}
        )
    }
}

@Composable
fun MantramSubTypesList(
    mantramSubTypes: List<MantramSubType>,
    onSelect: (MantramSubType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandItem by remember { mutableStateOf<MantramSubType?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(items = mantramSubTypes) { mantramSubType ->
            MantramSubCard(
                mantramSubType = mantramSubType,
                onExpandRequest = { expandItem = it },
                expand = expandItem == mantramSubType,
                onClick = { onSelect(mantramSubType) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MantramSubTypesListPreview() {
    MantramSesontenganTheme {
        MantramSubTypesList(
            mantramSubTypes = listOf(
                MantramSubType(1, "Test", "Test", "Test"),
                MantramSubType(2, "Test2", "Test2", "Test2"),
                MantramSubType(3, "Test3", "Test3", "Test3")
            ),
            onSelect = {},
            modifier = Modifier.width(400.dp)
        )
    }
}

@Composable
fun MantramSubCard(
    mantramSubType: MantramSubType,
    onClick: () -> Unit,
    onExpandRequest: (MantramSubType?) -> Unit,
    expand: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .animateContentSize()
            .clickable {
                onExpandRequest(if (expand) null else mantramSubType)
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = if (expand) Alignment.Top
                    else Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mantramSubType.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (!expand) {
                        Text(
                            text = mantramSubType.description.take(40),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Icon(
                    imageVector = if (expand) Icons.Filled.KeyboardArrowDown
                        else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
            
            if (expand) {
                Text(
                    text = mantramSubType.mantram.take(300),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onClick
                ) {
                    Text(
                        text = "Buka selengkapnya",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MantramSubCardPreview() {
    var isExpand by remember { mutableStateOf(false) }

    MantramSesontenganTheme {
        MantramSubCard(
            mantramSubType = MantramSubType(1, "Test", "Test", "Test"),
            onExpandRequest = { isExpand = !isExpand },
            expand = isExpand,
            onClick = {},
            modifier = Modifier.width(400.dp)
        )
    }
}