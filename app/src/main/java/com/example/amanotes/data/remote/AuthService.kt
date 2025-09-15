package com.example.amanotes.data.remote

import com.example.amanotes.data.remote.dto.AuthResponse
import com.example.amanotes.data.remote.dto.LoginRequest
import com.example.amanotes.data.remote.dto.SignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/signup")
    suspend fun signup(@Body body: SignupRequest): AuthResponse
}


