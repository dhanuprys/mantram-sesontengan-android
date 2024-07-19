package com.dedan.mantramsesontengan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dedan.mantramsesontengan.ui.navigation.GlobalViewModel
import com.dedan.mantramsesontengan.ui.theme.MantramSesontenganTheme

class MainActivity : ComponentActivity() {
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            globalViewModel = viewModel()

            MantramSesontenganTheme {
                MantramApp(globalViewModel = globalViewModel)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        globalViewModel.stopAudio()
    }
}