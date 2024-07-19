package com.dedan.mantramsesontengan

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedan.mantramsesontengan.ui.navigation.MantramNavHost
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme
import kotlinx.coroutines.launch

@Composable
fun MantramApp(
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
    canNavigateBack: Boolean = false
) {
    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            titleContentColor = MaterialTheme.colorScheme.primary,
//        ),
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
        title = {
            Text("Mantram Sesontengan")
        },
        modifier = modifier
    )
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
                topBar = { MantramAppBar(canNavigateBack = false) }
            ) {}
        }
    }
}