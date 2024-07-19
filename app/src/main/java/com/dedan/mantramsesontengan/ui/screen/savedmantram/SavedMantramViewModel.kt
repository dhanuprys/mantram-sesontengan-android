package com.dedan.mantramsesontengan.ui.screen.savedmantram

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedan.mantramsesontengan.data.repository.MantramRepository
import com.dedan.mantramsesontengan.data.repository.SavedMantramRepository
import com.dedan.mantramsesontengan.model.MantramSubType
import com.dedan.mantramsesontengan.model.SavedMantram
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedMantramViewModel(
    private val mantramRepository: MantramRepository,
    private val savedMantramRepository: SavedMantramRepository
) : ViewModel() {
    val savedMantramUiState = savedMantramRepository.getAllSavedMantramsStream()
        .map { SavedMantramUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavedMantramUiState()
        )
}

data class SavedMantramUiState(val savedMantramList: List<SavedMantram> = listOf())