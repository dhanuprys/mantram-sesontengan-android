package com.dedan.mantramsesontengan.ui.screen.savedmantram

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.dedan.mantramsesontengan.model.SavedMantram
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.navigation.AudioPlayerUiState
import com.dedan.mantramsesontengan.ui.navigation.GlobalViewModel
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme

object SavedMantramDestination : NavigationDestination {
    override val route = "saved_mantram"
    override val titleRes = R.string.app_name
}

@Composable
fun SavedMantramScreen(
    globalViewModel: GlobalViewModel,
    navigateToSavedMantramDetail: (Int, Int) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavedMantramViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val savedMantramUiState = viewModel.savedMantramUiState.collectAsState()
    val audioPlayerUiState = globalViewModel.audioPlayerUiState.collectAsState()

    Scaffold(
        topBar = {
            MantramAppBar(
                canNavigateBack = true,
                onNavigateUp = navigateUp
            ) {
                Text("Mantram Tersimpan")
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
        SavedMantramBody(
            savedMantramUiState = savedMantramUiState.value,
            onMantramSelect = { navigateToSavedMantramDetail(it.baseId, it.id) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SavedMantramBody(
    savedMantramUiState: SavedMantramUiState,
    onMantramSelect: (SavedMantram) -> Unit,
    modifier: Modifier = Modifier
) {
        SavedMantramList(
            savedMantrams = savedMantramUiState.savedMantramList,
            onSelect = onMantramSelect,
            modifier = modifier.padding(16.dp)
        )
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectSubBody_Success() {
    MantramSesontenganTheme {
        SavedMantramBody(
            savedMantramUiState = SavedMantramUiState(
                listOf(
                    SavedMantram(1, 1, "Test", "Test", "Test", version = 1),
                    SavedMantram(2, 1, "Test2", "Test2", "Test2", version = 1)
                )
            ),
            onMantramSelect = {}
        )
    }
}

@Composable
fun SavedMantramList(
    savedMantrams: List<SavedMantram>,
    onSelect: (SavedMantram) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandItem by remember { mutableStateOf<SavedMantram?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(items = savedMantrams) { mantram ->
            SavedMantramCard(
                mantram = mantram,
                onExpandRequest = { expandItem = it },
                expand = expandItem == mantram,
                onClick = { onSelect(mantram) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun SavedMantramListPreview() {
    MantramSesontenganTheme {
        SavedMantramList(
            savedMantrams = listOf(
                SavedMantram(1, 1, "Test", "Test", "Test", version = 1),
                SavedMantram(2, 1, "Test2", "Test2", "Test2", version = 1),
                SavedMantram(3, 1, "Test3", "Test3", "Test3", version = 1)
            ),
            onSelect = {},
            modifier = Modifier.width(400.dp)
        )
    }
}

@Composable
fun SavedMantramCard(
    mantram: SavedMantram,
    onClick: () -> Unit,
    onExpandRequest: (SavedMantram?) -> Unit,
    expand: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .animateContentSize()
            .clickable {
                onExpandRequest(if (expand) null else mantram)
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = if (expand) Alignment.Top
                    else Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mantram.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (!expand) {
                        Text(
                            text = mantram.description.take(40),
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
                    text = mantram.mantram.take(300),
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
fun SavedMantramCardPreview() {
    var isExpand by remember { mutableStateOf(false) }

    MantramSesontenganTheme {
        SavedMantramCard(
            mantram = SavedMantram(1, 1, "Test", "Test", "Test", version = 1),
            onExpandRequest = { isExpand = !isExpand },
            expand = isExpand,
            onClick = {},
            modifier = Modifier.width(400.dp)
        )
    }
}