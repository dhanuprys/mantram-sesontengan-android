package com.dedan.mantramsesontengan.ui.screen.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            "Info Aplikasi",
            modifier = Modifier.weight(1f)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.footer),
                contentDescription = null,
                modifier = Modifier.height(80.dp)
            )
        }
    }
}