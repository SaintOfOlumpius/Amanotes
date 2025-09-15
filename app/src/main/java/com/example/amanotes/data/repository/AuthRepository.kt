package com.example.amanotes.data.repository

import com.example.amanotes.data.local.UserDao
import com.example.amanotes.data.local.UserEntity
import com.example.amanotes.data.remote.AuthService
import com.example.amanotes.data.remote.dto.LoginRequest
import com.example.amanotes.data.remote.dto.SignupRequest

class AuthRepository(
    private val authService: AuthService,
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val response = authService.login(LoginRequest(email, password))
        val user = UserEntity(
            id = response.user.id,
            name = response.user.name,
            email = response.user.email,
            token = response.token
        )
        userDao.clear()
        userDao.upsert(user)
    }

    suspend fun signup(name: String, email: String, password: String): Result<Unit> = runCatching {
        val response = authService.signup(SignupRequest(name, email, password))
        val user = UserEntity(
            id = response.user.id,
            name = response.user.name,
            email = response.user.email,
            token = response.token
        )
        userDao.clear()
        userDao.upsert(user)
    }
}


