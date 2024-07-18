package com.dedan.mantramsesontengan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mantram")
data class Mantram(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val mantram: String,
    val description: String,
    val version: String
)