package com.dedan.mantramsesontengan

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedan.mantramsesontengan.ui.navigation.AudioPlayerUiState
import com.dedan.mantramsesontengan.ui.navigation.GlobalViewModel
import com.dedan.mantramsesontengan.ui.navigation.MantramNavHost
import com.dedan.mantramsesontengan.ui.screen.info.InfoDestination
import com.dedan.mantramsesontengan.ui.screen.mantramselectbase.MantramSelectBaseDestination
import com.dedan.mantramsesontengan.ui.screen.savedmantram.SavedMantramDestination
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme
import com.dedan.mantramsesontengan.ui.theme.TypographySize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MantramApp(
    globalViewModel: GlobalViewModel,
    navController: NavHostController = rememberNavController()
) {
    val typographySize by globalViewModel.typographySize.collectAsState()
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val toastMessage by globalViewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            globalViewModel.showToast(null)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            MantramAppDrawer(
                currentFontSize = typographySize,
                changeFontSize = { globalViewModel.changeTypography(it) },
                redirectTo = { route, autoClose ->
                    navController.navigate(route)

                    if (autoClose) {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                },
                requestClose = { coroutineScope.launch { drawerState.close() } }
            )
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
    audioPlayerUiState: AudioPlayerUiState,
    modifier: Modifier = Modifier,
    onPlayRequest: () -> Unit = {},
    onStopRequest: () -> Unit = {},
    onRestartRequest: () -> Unit = {}
) {
    val isAudioReady by remember(audioPlayerUiState) {
        derivedStateOf {
            audioPlayerUiState.let {
                it is AudioPlayerUiState.Paused || it is AudioPlayerUiState.Playing
            }
        }
    }

//    LaunchedEffect(audioPlayerUiState) {
//        if (audioPlayerUiState is AudioPlayerUiState.Playing) {
//            Log.d("AudioBottomBar", "It's playing bro")
//            while (true) {
//                Log.d("AudioBottomBar", "current progress: ${audioPlayerUiState.audioPlayer.currentPosition}")
//                delay(1000)
//            }
//        } else {
//            Log.d("AudioBottomBar", "It's not playing bro")
//        }
//    }

    BottomAppBar(
        containerColor =
            when (audioPlayerUiState) {
                is AudioPlayerUiState.Loading -> MaterialTheme.colorScheme.surfaceVariant
                is AudioPlayerUiState.Paused -> MaterialTheme.colorScheme.inversePrimary
                is AudioPlayerUiState.Blank -> MaterialTheme.colorScheme.surfaceVariant
                is AudioPlayerUiState.Playing -> MaterialTheme.colorScheme.primaryContainer
                is AudioPlayerUiState.Error -> MaterialTheme.colorScheme.surfaceVariant
            },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (
                audioPlayerUiState is AudioPlayerUiState.Loading
                || audioPlayerUiState is AudioPlayerUiState.Playing
                ) {
                LinearProgressIndicator(
                    progress = {
                        if (audioPlayerUiState is AudioPlayerUiState.Loading)
                            audioPlayerUiState.progress
                        else if (audioPlayerUiState is AudioPlayerUiState.Playing)
                            audioPlayerUiState.progress
                        else
                            0f
                    },
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
fun FontSelectorItem(
    selected: Boolean,
    fontSize: TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        icon = {
            Text("A", fontSize = fontSize)
        },
        label = {
            Text("Besar", fontSize = fontSize)
        },
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun MantramAppDrawer(
    redirectTo: (String, Boolean) -> Unit,
    currentFontSize: TypographySize,
    changeFontSize: (TypographySize) -> Unit,
    requestClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val itemPadding = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)

    ModalDrawerSheet(modifier = modifier) {
        NavigationDrawerItem(
            icon = {
                Icon(imageVector = Icons.Filled.Home, contentDescription = null)
            },
            label = {
                Text("Beranda")
            },
            selected = true,
            onClick = { redirectTo(MantramSelectBaseDestination.route, true) },
            modifier = itemPadding
        )
        NavigationDrawerItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = null)
            },
            label = {
                Text("Mantram Tersimpan")
            },
            selected = false,
            onClick = { redirectTo(SavedMantramDestination.route, true) },
            modifier = itemPadding
        )
        NavigationDrawerItem(
            icon = {
                Icon(imageVector = Icons.Filled.Info, contentDescription = null)
            },
            label = {
                Text("Info Aplikasi")
            },
            selected = false,
            onClick = { redirectTo(InfoDestination.route, true) },
            modifier = itemPadding
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Ukuran font",
            modifier = itemPadding
        )
        Spacer(modifier = Modifier.height(20.dp))
        FontSelectorItem(
            selected = currentFontSize == TypographySize.SMALL,
            fontSize = 14.sp,
            onClick = {
                changeFontSize(TypographySize.SMALL)
                requestClose()
            },
            modifier = itemPadding
        )
        FontSelectorItem(
            selected = currentFontSize == TypographySize.NORMAL,
            fontSize = 16.sp,
            onClick = {
                changeFontSize(TypographySize.NORMAL)
                requestClose()
            },
            modifier = itemPadding
        )
        FontSelectorItem(
            selected = currentFontSize == TypographySize.LARGE,
            fontSize = 20.sp,
            onClick = {
                changeFontSize(TypographySize.LARGE)
                requestClose()
            },
            modifier = itemPadding
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun MantramAppBarPreview() {
    val drawerState = DrawerState(DrawerValue.Open)
    MantramSesontenganTheme {
        ModalNavigationDrawer(
            drawerContent = {
                MantramAppDrawer(
                    redirectTo = { _, _ -> },
                    currentFontSize = TypographySize.LARGE,
                    changeFontSize = {}
                )
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    MantramAppBar(canNavigateBack = false) {
                        Text("Mantram Sesontengan")
                    }
                },
                bottomBar = { AudioBottomBar(audioPlayerUiState = AudioPlayerUiState.Paused) }
            ) {}
        }
    }
}