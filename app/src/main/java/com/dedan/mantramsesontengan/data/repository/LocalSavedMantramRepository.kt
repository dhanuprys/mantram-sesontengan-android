package com.dedan.mantramsesontengan.data.repository

import com.dedan.mantramsesontengan.data.db.SavedMantramDao
import com.dedan.mantramsesontengan.model.SavedMantram
import kotlinx.coroutines.flow.Flow

class LocalSavedMantramRepository(
    private val savedMantramDao: SavedMantramDao
) : SavedMantramRepository {
    override suspend fun getAllSavedMantramsStream(): Flow<gList<SavedMantram>> {
       return savedMantramDao.getAllSavedMantrams()
    }

    override suspend fun getMantramStream(id: Int): Flow<SavedMantram?> {
        return savedMantramDao.getMantramById(id)
    }

    override suspend fun deleteMantram(mantram: SavedMantram) {
        return savedMantramDao.deleteMantram(mantram)
    }

    override suspend fun updateMantram(mantram: SavedMantram) {
        return savedMantramDao.updateMantram(mantram)
    }
}