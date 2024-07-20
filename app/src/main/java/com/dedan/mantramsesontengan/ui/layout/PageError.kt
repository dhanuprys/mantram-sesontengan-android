package com.dedan.mantramsesontengan.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dedan.mantramsesontengan.R
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme

@Composable
fun PageError(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_sad),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )
        Text("Gagal memuat data")
    }
}

@Preview(showSystemUi = true)
@Composable
fun PageErrorPreview() {
    MantramSesontenganTheme {
        PageError(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}