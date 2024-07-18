package com.dedan.mantramsesontengan.model

import kotlinx.serialization.Serializable

@Serializable
data class MantramSubType(
    val id: Int,
    val name: String,
    val mantram: String,
    val description: String
)