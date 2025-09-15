package com.example.amanotes.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: RemoteUser
)

data class RemoteUser(
    val id: String,
    val name: String,
    val email: String
)


