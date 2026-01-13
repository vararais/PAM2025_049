package com.example.savitapp

import android.app.Application
import com.example.savitapp.repository.AuthRepository
import com.example.savitapp.repository.NetworkAuthRepository
import com.example.savitapp.repository.NetworkStuffRepository
import com.example.savitapp.repository.StuffRepository
import com.example.savitapp.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

// Mirip ContainerApp di repo referensi
interface AppContainer {
    val authRepository: AuthRepository
    val stuffRepository: StuffRepository
}

class DefaultAppContainer : AppContainer {
    // GANTI IP INI SESUAI IP KOMPUTER (Jika pakai HP Asli) atau 10.0.2.2 (Emulator)
    private val baseUrl = "http://10.0.2.2:3000/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        NetworkAuthRepository(retrofitService)
    }

    override val stuffRepository: StuffRepository by lazy {
        NetworkStuffRepository(retrofitService)
    }
}

class SavitApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}