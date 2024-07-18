package com.dedan.mantramsesontengan.data.repository

import com.dedan.mantramsesontengan.model.MantramBaseType
import com.dedan.mantramsesontengan.model.MantramDetail
import com.dedan.mantramsesontengan.model.MantramSubType
import com.dedan.mantramsesontengan.network.MantramApiService

class NetworkMantramRepository(
    private val mantramApiService: MantramApiService
) : MantramRepository {
    override suspend fun getMantramTypes(): List<MantramBaseType> =
        mantramApiService.getMantramTypes()

    override suspend fun getMantramSubTypes(mantramBaseId: Int): List<MantramSubType> =
        mantramApiService.getMantramSubTypes(mantramBaseId)

    override suspend fun getMantramDetail(mantramBaseId: Int, mantramId: Int): List<MantramDetail> =
        mantramApiService.getMantramDetail(mantramBaseId, mantramId)
}