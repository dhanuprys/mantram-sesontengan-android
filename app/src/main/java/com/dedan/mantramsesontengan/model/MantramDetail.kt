package com.dedan.mantramsesontengan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MantramDetail(
    val id: Int,
    val name: String,
    val mantram: String,
    val description: String,
    @SerialName("audio_url")
    val audioUrl: String?,
    val version: Int = 0,
    @SerialName("updated_at")
    val updatedAt: String
)

fun MantramDetail.toSavedMantram(baseId: Int): SavedMantram {
    return SavedMantram(id, baseId, name, mantram, description, version ?: 0)
}