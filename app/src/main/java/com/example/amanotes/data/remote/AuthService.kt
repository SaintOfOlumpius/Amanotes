package com.example.amanotes.data.remote

import com.example.amanotes.data.remote.dto.ReqresAuthResponse
import com.example.amanotes.data.remote.dto.ReqresLoginRequest
import com.example.amanotes.data.remote.dto.ReqresRegisterRequest
import com.example.amanotes.data.remote.dto.ReqresUsersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/login")
    suspend fun login(@Body body: ReqresLoginRequest): ReqresAuthResponse

    @POST("api/register")
    suspend fun register(@Body body: ReqresRegisterRequest): ReqresAuthResponse
    
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int = 1): ReqresUsersResponse
}


