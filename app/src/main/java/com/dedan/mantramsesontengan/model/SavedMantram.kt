package com.dedan.mantramsesontengan.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_mantram")
data class SavedMantram(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "base_id")
    val baseId: Int,
    val name: String,
    val mantram: String,
    val description: String,
    val version: Int
)

fun SavedMantram.toMantramDetail() =
    MantramDetail(id, name, mantram, description, null, 0, "")