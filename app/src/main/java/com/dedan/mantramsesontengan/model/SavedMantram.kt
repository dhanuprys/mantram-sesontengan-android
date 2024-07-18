package com.dedan.mantramsesontengan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_mantram")
data class SavedMantram(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val mantram: String,
    val description: String,
    val version: String
)