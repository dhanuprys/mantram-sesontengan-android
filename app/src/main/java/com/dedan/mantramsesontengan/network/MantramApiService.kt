package com.dedan.mantramsesontengan.network

import com.dedan.mantramsesontengan.model.MantramBaseType
import com.dedan.mantramsesontengan.model.MantramDetail
import com.dedan.mantramsesontengan.model.MantramSubType
import retrofit2.http.GET
import retrofit2.http.Path

interface MantramApiService {
    @GET("api/v1/mantram")
    suspend fun getMantramTypes(): List<MantramBaseType>

    @GET("api/v1/mantram/{mantramBaseId}")
    suspend fun getMantramSubTypes(
        @Path("mantramBaseId") mantramBaseId: Int
    ): List<MantramSubType>

    @GET("api/v1/mantram/{mantramBaseId}/{mantramId}")
    suspend fun getMantramDetail(
        @Path("mantramBaseId") mantramBaseId: Int,
        @Path("mantramId") mantramId: Int
    ): List<MantramDetail>
}