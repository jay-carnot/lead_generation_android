package com.carnot.leadgeneration.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @GET("Login")
    suspend fun login(@Header("Authorization") authToken: String): LoginResponse

    // Define other authentication endpoints such as register, logout, etc.
}
