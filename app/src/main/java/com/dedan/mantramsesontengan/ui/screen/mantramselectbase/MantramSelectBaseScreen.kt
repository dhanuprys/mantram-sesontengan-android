package com.dedan.mantramsesontengan.ui.screen.mantramselectbase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.model.MantramBaseType
import com.dedan.mantramsesontengan.ui.AppViewModelProvider
import com.dedan.mantramsesontengan.ui.layout.PageError
import com.dedan.mantramsesontengan.ui.layout.PageLoading
import com.dedan.mantramsesontengan.ui.navigation.NavigationDestination
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme

object MantramSelectBaseDestination : NavigationDestination {
    override val route = "mantram_select_base"
    override val titleRes = R.string.app_name
}

@Composable
fun MantramSelectBaseScreen(
    onMantramSelect: (MantramBaseType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MantramSelectBaseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(modifier = modifier) { innerPadding ->
        MantramSelectBaseBody(
            mantramTypesUiState = viewModel.mantramTypesUiState,
            onSelect = onMantramSelect,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MantramSelectBaseBody(
    mantramTypesUiState: MantramTypesUiState,
    onSelect: (MantramBaseType) -> Unit,
    modifier: Modifier = Modifier
) {
    when (mantramTypesUiState) {
        is MantramTypesUiState.Error -> PageError(modifier = modifier)
        is MantramTypesUiState.Loading -> PageLoading(modifier = modifier)
        is MantramTypesUiState.Success -> MantramTypesList(
            mantramTypes = mantramTypesUiState.data,
            onSelect = onSelect,
            modifier = modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectBaseBodyPreview_Loading() {
    MantramSesontenganTheme {
        MantramSelectBaseBody(
            mantramTypesUiState = MantramTypesUiState.Loading,
            onSelect = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectBaseBodyPreview_Error() {
    MantramSesontenganTheme {
        MantramSelectBaseBody(
            mantramTypesUiState = MantramTypesUiState.Error,
            onSelect = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MantramSelectBaseBodyPreview_Success() {
    MantramSesontenganTheme {
        MantramSelectBaseBody(
            mantramTypesUiState = MantramTypesUiState.Success(
                listOf(
                    MantramBaseType(1, "Test", mantramCount = 5),
                    MantramBaseType(2, "Test2", mantramCount = 10),
                    MantramBaseType(3, "Test3", mantramCount = 15)
                )
            ),
            onSelect = {}
        )
    }
}

@Composable
fun MantramTypesList(
    mantramTypes: List<MantramBaseType>,
    onSelect: (MantramBaseType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(items = mantramTypes) {
            MantramTypeCard(
                mantramType = it,
                onClick = { onSelect(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MantramTypesListPreview() {
    MantramSesontenganTheme {
        MantramTypesList(
            mantramTypes = listOf(
                MantramBaseType(1, "Test", mantramCount = 5),
                MantramBaseType(2, "Test2", mantramCount = 10),
                MantramBaseType(3, "Test3", mantramCount = 15)
            ),
            onSelect = {},
            modifier = Modifier.width(400.dp)
        )
    }
}

@Composable
fun MantramTypeCard(
    mantramType: MantramBaseType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = mantramType.name)
                Text(text = "Mantram Count: ${mantramType.mantramCount}")
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun MantramTypeCardPreview() {
    MantramSesontenganTheme {
        MantramTypeCard(
            mantramType = MantramBaseType(1, "Test", mantramCount = 5),
            onClick = {},
            modifier = Modifier.width(400.dp)
        )
    }
}