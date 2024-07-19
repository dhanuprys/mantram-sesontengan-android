package com.dedan.mantramsesontengan.data.repository

import com.dedan.mantramsesontengan.model.SavedMantram
import kotlinx.coroutines.flow.Flow

interface SavedMantramRepository {
    fun getAllSavedMantramsStream(): Flow<List<SavedMantram>>
    fun getMantramStream(id: Int): Flow<SavedMantram?>
    suspend fun deleteMantramById(id: Int)
    suspend fun deleteMantram(mantram: SavedMantram)
    suspend fun updateMantram(mantram: SavedMantram)
    suspend fun saveMantram(mantram: SavedMantram)
}