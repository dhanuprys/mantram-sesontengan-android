package com.dedan.mantramsesontengan.ui.screen.mantramdetail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedan.mantramsesontengan.data.repository.MantramRepository
import com.dedan.mantramsesontengan.data.repository.SavedMantramRepository
import com.dedan.mantramsesontengan.model.MantramDetail
import kotlinx.coroutines.launch

class MantramDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val mantramRepository: MantramRepository,
    private val savedMantramRepository: SavedMantramRepository
) : ViewModel() {
    private val mantramBaseId: Int =
        checkNotNull(savedStateHandle[MantramDetailDestination.mantramBaseIdArg])
    private val mantramId: Int =
        checkNotNull(savedStateHandle[MantramDetailDestination.mantramIdArg])

    var mantramDetailUiState: MantramDetailUiState by mutableStateOf(MantramDetailUiState.Loading)
        private set

    init {
        getMantramDetail()
    }

    fun getMantramDetail() {
        viewModelScope.launch {
            mantramDetailUiState = MantramDetailUiState.Loading
            mantramDetailUiState = try {
                val mantramDetail = mantramRepository.getMantramDetail(mantramBaseId, mantramId)
                MantramDetailUiState.Success(mantramDetail)
            } catch (e: Exception) {
                Log.e("MantramDetailViewModel", "Error fetching mantram detail", e)
                MantramDetailUiState.Error
            }
        }
    }
}

sealed interface MantramDetailUiState {
    data class Success(val data: MantramDetail) : MantramDetailUiState
    object Error : MantramDetailUiState
    object Loading : MantramDetailUiState
}