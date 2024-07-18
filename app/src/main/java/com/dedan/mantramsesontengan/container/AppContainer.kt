package com.dedan.mantramsesontengan.container

import com.dedan.mantramsesontengan.data.repository.MantramRepository
import com.dedan.mantramsesontengan.data.repository.SavedMantramRepository

interface AppContainer {
    val mantramRepository: MantramRepository
    val savedMantramRepository: SavedMantramRepository
}