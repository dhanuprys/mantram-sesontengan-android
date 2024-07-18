package com.dedan.mantramsesontengan.ui.screen.mantramselectbase

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedan.mantramsesontengan.data.repository.MantramRepository
import com.dedan.mantramsesontengan.model.MantramBaseType
import kotlinx.coroutines.launch

class MantramSelectBaseViewModel(
    private val mantramRepository: MantramRepository
) : ViewModel() {
    var mantramTypesUiState: MantramTypesUiState by mutableStateOf(MantramTypesUiState.Loading)
        private set

    init {
        getMantramTypes()
    }

    fun getMantramTypes() {
        viewModelScope.launch {
            mantramTypesUiState = MantramTypesUiState.Loading
            mantramTypesUiState = try {
                val mantrams = mantramRepository.getMantramTypes()
                MantramTypesUiState.Success(mantrams)
            } catch (e: Exception) {
                Log.e("MantramSelectBaseViewModel", "Error fetching mantram types", e)
                MantramTypesUiState.Error
            }
        }
    }
}

sealed interface MantramTypesUiState {
    data class Success(val data: List<MantramBaseType>) : MantramTypesUiState
    object Error : MantramTypesUiState
    object Loading : MantramTypesUiState
}