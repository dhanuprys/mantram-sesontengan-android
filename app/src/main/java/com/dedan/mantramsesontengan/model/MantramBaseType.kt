package com.dedan.mantramsesontengan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MantramBaseType(
    val id: Int,
    val name: String,
    @SerialName("mantram_count")
    val mantramCount: Int
)
