package com.dedan.mantramsesontengan

import android.app.Application
import com.dedan.mantramsesontengan.container.AppContainer
import com.dedan.mantramsesontengan.container.DefaultAppContainer

class MantramApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}