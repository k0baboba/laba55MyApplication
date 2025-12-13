package com.example.laba55myapplication

import retrofit2.Response
import retrofit2.http.GET

interface OpenSkyApiService {
    @GET("states/all") // берём состояние всех самолётов, не блокируем интерфейс
    suspend fun getAllStates(): Response<OpenSkyResponse>
}