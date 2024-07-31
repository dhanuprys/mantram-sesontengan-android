package com.dedan.mantramsesontengan.ui.screen.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dedan.mantramsesontengan.MantramAppBar
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination

object InfoDestination : NavigationDestination {
    override val route = "info"
    override val titleRes = R.string.app_name
}

@Composable
fun InfoScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MantramAppBar(
                canNavigateBack = true,
                onNavigateUp = navigateUp
            ) {
                Text("Info Aplikasi")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        InfoBody(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun InfoBody(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Info Aplikasi")
    }
}