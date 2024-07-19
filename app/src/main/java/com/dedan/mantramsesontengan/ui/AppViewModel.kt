package com.dedan.mantramsesontengan.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dedan.mantramsesontengan.MantramApplication
import com.dedan.mantramsesontengan.ui.screen.mantramdetail.MantramDetailViewModel
import com.dedan.mantramsesontengan.ui.screen.mantramselectbase.MantramSelectBaseViewModel
import com.dedan.mantramsesontengan.ui.screen.mantramselectsub.MantramSelectSubViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MantramSelectBaseViewModel(
                mantramRepository = mantramApplication().container.mantramRepository
            )
        }

        initializer {
            MantramSelectSubViewModel(
                this.createSavedStateHandle(),
                mantramRepository = mantramApplication().container.mantramRepository
            )
        }

        initializer {
            MantramDetailViewModel(
                this.createSavedStateHandle(),
                mantramRepository = mantramApplication().container.mantramRepository,
                savedMantramRepository = mantramApplication().container.savedMantramRepository
            )
        }
    }
}

fun CreationExtras.mantramApplication(): MantramApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MantramApplication)