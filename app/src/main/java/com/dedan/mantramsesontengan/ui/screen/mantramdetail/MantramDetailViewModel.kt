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
import com.dedan.mantramsesontengan.model.toSavedMantram
import kotlinx.coroutines.flow.firstOrNull
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
    var mantramSavedStatusUiState: MantramSavedStatusUiState by mutableStateOf(
        MantramSavedStatusUiState.Unknown
    )
        private set

    init {
        getMantramDetail()
    }

    fun storeInBookmark() {
        viewModelScope.launch {
            if (mantramDetailUiState is MantramDetailUiState.Success) {
                Log.d("MantramDetail", "Storing in database")
                savedMantramRepository.saveMantram(
                    (mantramDetailUiState as MantramDetailUiState.Success).data.toSavedMantram()
                )
                Log.d("MantramDetail", "Successfully store")
                mantramSavedStatusUiState = MantramSavedStatusUiState.Saved
            }
        }
    }

    fun removeFromBookmark() {
        viewModelScope.launch {
            savedMantramRepository.deleteMantramById(mantramId)
            mantramSavedStatusUiState = MantramSavedStatusUiState.NotSaved
        }
    }

    fun getMantramDetail() {
        viewModelScope.launch {
            mantramDetailUiState = MantramDetailUiState.Loading
            mantramDetailUiState = try {
                val mantramDetail = mantramRepository.getMantramDetail(mantramBaseId, mantramId)

                savedMantramRepository.getMantramStream(mantramId)
                    .firstOrNull()
                    .also {
                        if (it == null) {
                            mantramSavedStatusUiState = MantramSavedStatusUiState.NotSaved
                            return@also
                        }

                        mantramSavedStatusUiState = if (it.version != mantramDetail.version)
                            MantramSavedStatusUiState.NeedUpdate
                        else
                            MantramSavedStatusUiState.Saved
                    }

                MantramDetailUiState.Success(mantramDetail)
            } catch (e: Exception) {
                Log.e("MantramDetailViewModel", "Error fetching mantram detail", e)
                MantramDetailUiState.Error
            }
        }
    }
}

sealed interface MantramSavedStatusUiState {
    object NeedUpdate : MantramSavedStatusUiState
    object Saved : MantramSavedStatusUiState
    object NotSaved : MantramSavedStatusUiState
    object Unknown : MantramSavedStatusUiState
}

sealed interface MantramDetailUiState {
    data class Success(val data: MantramDetail) : MantramDetailUiState
    object Error : MantramDetailUiState
    object Loading : MantramDetailUiState
}