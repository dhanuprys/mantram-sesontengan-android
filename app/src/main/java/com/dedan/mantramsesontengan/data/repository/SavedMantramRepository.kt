package com.dedan.mantramsesontengan.data.repository

import com.dedan.mantramsesontengan.model.SavedMantram
import kotlinx.coroutines.flow.Flow

interface SavedMantramRepository {
    suspend fun getAllSavedMantramsStream(): Flow<List<SavedMantram>>
    suspend fun getMantramStream(id: Int): Flow<SavedMantram?>
    suspend fun deleteMantram(mantram: SavedMantram)
    suspend fun updateMantram(mantram: SavedMantram)
}