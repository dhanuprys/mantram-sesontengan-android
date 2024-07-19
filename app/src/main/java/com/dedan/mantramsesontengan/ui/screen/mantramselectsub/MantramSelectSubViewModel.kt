package com.dedan.mantramsesontengan.ui.screen.mantramselectsub

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedan.mantramsesontengan.data.repository.MantramRepository
import com.dedan.mantramsesontengan.model.MantramSubType
import kotlinx.coroutines.launch

class MantramSelectSubViewModel(
    savedStateHandle: SavedStateHandle,
    private val mantramRepository: MantramRepository
) : ViewModel() {
    private val mantramBaseId: Int = checkNotNull(savedStateHandle[MantramSelectSubDestination.mantramBaseIdArg])

    var mantramSubTypesUiState: MantramSubTypesUiState by mutableStateOf(MantramSubTypesUiState.Loading)
        private set

    init {
        getMantramSubTypes()
    }

    fun getMantramSubTypes() {
        viewModelScope.launch {
            mantramSubTypesUiState = MantramSubTypesUiState.Loading
            mantramSubTypesUiState = try {
                val mantramSubTypes = mantramRepository.getMantramSubTypes(mantramBaseId)
                MantramSubTypesUiState.Success(mantramSubTypes)
            } catch (e: Exception) {
                Log.e("MantramSelectSubViewModel", "Error fetching mantram sub types", e)
                MantramSubTypesUiState.Error
            }
        }
    }
}

sealed interface MantramSubTypesUiState {
    data class Success(val data: List<MantramSubType>) : MantramSubTypesUiState
    object Error : MantramSubTypesUiState
    object Loading : MantramSubTypesUiState
}