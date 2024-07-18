package com.dedan.mantramsesontengan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MantramDetail(
    val id: Int,
    val name: String,
    val mantram: String,
    val description: String,
    val version: String,
    @SerialName("updated_at")
    val updatedAt: String
)