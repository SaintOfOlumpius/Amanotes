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

// JSONPlaceholder user model matching their API
data class JSONPlaceholderUser(
    val id: Int? = null,
    val name: String,
    val username: String,
    val email: String,
    val phone: String = "",
    val website: String = "",
    val address: Address? = null,
    val company: Company? = null
)

data class Address(
    val street: String = "",
    val suite: String = "",
    val city: String = "",
    val zipcode: String = "",
    val geo: Geo? = null
)

data class Geo(
    val lat: String = "",
    val lng: String = ""
)

data class Company(
    val name: String = "",
    val catchPhrase: String = "",
    val bs: String = ""
)

// Reqres.in API models
data class ReqresLoginRequest(
    val email: String,
    val password: String
)

data class ReqresRegisterRequest(
    val email: String,
    val password: String
)

data class ReqresAuthResponse(
    val token: String? = null,
    val id: Int? = null,
    val error: String? = null
)

data class ReqresUsersResponse(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<ReqresUser>,
    val support: ReqresSupport
)

data class ReqresUser(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

data class ReqresSupport(
    val url: String,
    val text: String
)


