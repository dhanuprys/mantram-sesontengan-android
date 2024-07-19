package com.dedan.mantramsesontengan.data.repository

import com.dedan.mantramsesontengan.model.MantramBaseType
import com.dedan.mantramsesontengan.model.MantramDetail
import com.dedan.mantramsesontengan.model.MantramSubType

interface MantramRepository {
    suspend fun getMantramTypes(): List<MantramBaseType>
    suspend fun getMantramSubTypes(mantramBaseId: Int): List<MantramSubType>
    suspend fun getMantramDetail(mantramBaseId: Int, mantramId: Int): MantramDetail
}