package com.dedan.mantramsesontengan.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dedan.mantramsesontengan.model.SavedMantram
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedMantramDao {
    @Query("SELECT * FROM saved_mantram")
    fun getAllSavedMantrams(): Flow<List<SavedMantram>>

    @Query("SELECT * FROM saved_mantram WHERE id = :id")
    fun getMantramById(id: Int): Flow<SavedMantram>

    @Delete
    suspend fun deleteMantram(mantram: SavedMantram)

    @Query("DELETE FROM saved_mantram WHERE id = :id")
    suspend fun deleteMantramById(id: Int)

    @Update
    suspend fun updateMantram(mantram: SavedMantram)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMantram(mantram: SavedMantram)
}