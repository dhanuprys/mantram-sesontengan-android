package com.dedan.mantramsesontengan

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedan.mantramsesontengan.ui.navigation.AudioPlayerUiState
import com.dedan.mantramsesontengan.ui.navigation.GlobalViewModel
import com.dedan.mantramsesontengan.ui.navigation.MantramNavHost
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme
import kotlinx.coroutines.launch

@Composable
fun MantramApp(
    globalViewModel: GlobalViewModel,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            MantramAppDrawer()
        }
    ) {
        MantramNavHost(
            globalViewModel = globalViewModel,
            onDrawerOpenRequest = {
                coroutineScope.launch {
                    drawerState.open()
                }
            },
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantramAppBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onDrawerOpenRequest: () -> Unit = {},
    canNavigateBack: Boolean = false,
    content: @Composable () -> Unit = {},
) {
    TopAppBar(
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            } else {
                IconButton(onClick = onDrawerOpenRequest) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null
                    )
                }
            }
        },
        title = { content() },
        modifier = modifier
    )
}

@Composable
fun AudioBottomBar(
    onPlayRequest: () -> Unit = {},
    onStopRequest: () -> Unit = {},
    onRestartRequest: () -> Unit = {},
    audioPlayerUiState: AudioPlayerUiState,
    modifier: Modifier = Modifier
) {
    val isAudioReady = audioPlayerUiState.let {
        it is AudioPlayerUiState.Paused || it is AudioPlayerUiState.Playing
    }

    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (audioPlayerUiState is AudioPlayerUiState.Loading) {
                LinearProgressIndicator(
                    progress = { audioPlayerUiState.progress.toFloat() / 100 },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    when (audioPlayerUiState) {
                        is AudioPlayerUiState.Loading -> {
                            Text("Memuat audio...")
                        }
                        is AudioPlayerUiState.Paused -> {
                            Text("Putar audio")
                        }
                        is AudioPlayerUiState.Blank -> {
                            Text("Audio tidak tersedia")
                        }
                        is AudioPlayerUiState.Playing -> {
                            Text("Sedang memutar audio")
                        }
                        is AudioPlayerUiState.Error -> {
                            Text("Audio gagal dimuat")
                        }
                    }
                }
                Row {
                    IconButton(
                        onClick = onRestartRequest,
                        enabled = isAudioReady,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            if (audioPlayerUiState is AudioPlayerUiState.Paused) {
                                onPlayRequest()
                            } else {
                                onStopRequest()
                            }
                        },
                        enabled = isAudioReady,
                        modifier = Modifier.background(Color.Black, shape = CircleShape)
                    ) {
                        Icon(
                            painter =
                                if (audioPlayerUiState is AudioPlayerUiState.Playing) {
                                    painterResource(id = R.drawable.ic_pause)
                                } else {
                                    painterResource(id = R.drawable.ic_play)
                                },
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MantramAppDrawer() {
    ModalDrawerSheet {
        Text("Hello")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun MantramAppBarPreview() {
    MantramSesontenganTheme {
        ModalNavigationDrawer(
            drawerContent = {
                MantramAppDrawer()
            }
        ) {
            Scaffold(
                topBar = {
                    MantramAppBar(canNavigateBack = false) {
                        Text("Mantram Sesontengan")
                    }
                },
                bottomBar = { AudioBottomBar(audioPlayerUiState = AudioPlayerUiState.Playing) }
            ) {}
        }
    }
}