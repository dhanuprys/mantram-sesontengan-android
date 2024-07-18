package com.dedan.mantramsesontengan.data.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MantramDao {
    @Query("SELECT * FROM mantram")
}