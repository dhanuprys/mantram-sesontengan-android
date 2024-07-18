package com.dedan.mantramsesontengan.container

import android.content.Context
import com.dedan.mantramsesontengan.data.db.MantramDatabase
import com.dedan.mantramsesontengan.data.repository.LocalSavedMantramRepository
import com.dedan.mantramsesontengan.data.repository.MantramRepository
import com.dedan.mantramsesontengan.data.repository.NetworkMantramRepository
import com.dedan.mantramsesontengan.data.repository.SavedMantramRepository
import com.dedan.mantramsesontengan.network.MantramApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultAppContainer(context: Context) : AppContainer {
    private val baseUrl = "https://mantram.suryamahendra.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitService by lazy {
        retrofit.create(MantramApiService::class.java)
    }

    override val mantramRepository: MantramRepository by lazy {
        NetworkMantramRepository(retrofitService)
    }

    override val savedMantramRepository: SavedMantramRepository by lazy {
        LocalSavedMantramRepository(MantramDatabase.getDatabase(context).savedMantramDao())
    }
}