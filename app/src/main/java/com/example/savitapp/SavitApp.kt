package com.example.savitapp

import android.app.Application
import com.example.savitapp.repository.AuthRepository
import com.example.savitapp.repository.NetworkAuthRepository
import com.example.savitapp.repository.NetworkStuffRepository
import com.example.savitapp.repository.StuffRepository
import com.example.savitapp.service.ApiConfig

interface AppContainer {
    val authRepository: AuthRepository
    val stuffRepository: StuffRepository
}

class DefaultAppContainer : AppContainer {
    private val apiService = ApiConfig.getApiService()

    override val authRepository: AuthRepository by lazy {
        NetworkAuthRepository(apiService)
    }

    override val stuffRepository: StuffRepository by lazy {
        NetworkStuffRepository(apiService)
    }
}

class SavitApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}