package com.example.amanotes.data.repository

import com.example.amanotes.data.local.UserDao
import com.example.amanotes.data.local.UserEntity
import com.example.amanotes.data.remote.AuthService
import com.example.amanotes.data.remote.dto.ReqresLoginRequest
import com.example.amanotes.data.remote.dto.ReqresRegisterRequest
import kotlinx.coroutines.flow.firstOrNull

class AuthRepository(
    private val authService: AuthService,
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        // Basic validation 
        if (email.isBlank() || password.isBlank()) {
            throw Exception("Email and password are required")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw Exception("Please enter a valid email address")
        }
        
        // First check if this is a locally registered user
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            // User exists locally (from signup), just validate password length and log them in
            if (password.length >= 6) {
                val updatedUser = existingUser.copy(
                    token = "local_token_${System.currentTimeMillis()}"
                )
                userDao.clear() // Clear any other users
                userDao.upsert(updatedUser) // Set as current user
                return@runCatching
            } else {
                throw Exception("Password must be at least 6 characters")
            }
        }
        
        // If not local user, try reqres.in API for demo accounts
        if (email.equals("eve.holt@reqres.in", ignoreCase = true) && password == "cityslicka") {
            try {
                // Make actual login request to reqres.in
                val loginRequest = ReqresLoginRequest(email, password)
                val authResponse = authService.login(loginRequest)
                
                // Check if login was successful
                if (authResponse.error != null) {
                    throw Exception(authResponse.error)
                }
                
                // Verify we got a token back
                if (authResponse.token == null) {
                    throw Exception("Login failed: No token received")
                }
                
                // Get user details from reqres.in users API
                val usersResponse = authService.getUsers(page = 1)
                val foundUser = usersResponse.data.find { it.email.equals(email, ignoreCase = true) }
                
                val userName = if (foundUser != null) {
                    "${foundUser.first_name} ${foundUser.last_name}"
                } else {
                    "Demo User"
                }
                
                // Store user in local database
                val user = UserEntity(
                    id = foundUser?.id?.toString() ?: "reqres_user_${System.currentTimeMillis()}",
                    name = userName,
                    email = email,
                    token = authResponse.token
                )
                userDao.clear()
                userDao.upsert(user)
                
            } catch (e: retrofit2.HttpException) {
                val errorMessage = try {
                    e.response()?.errorBody()?.string() ?: "Unknown error"
                } catch (ex: Exception) {
                    "HTTP ${e.code()}"
                }
                
                when (e.code()) {
                    400 -> throw Exception("Invalid credentials. For reqres.in demo use: eve.holt@reqres.in / cityslicka")
                    431 -> throw Exception("Request too large. Please try again.")
                    else -> throw Exception("Login failed (${e.code()}): $errorMessage")
                }
            }
        } else {
            throw Exception("User not found. Please sign up first, or use demo: eve.holt@reqres.in / cityslicka")
        }
    }

    suspend fun signup(name: String, email: String, password: String): Result<Unit> = runCatching {
        // Basic validation
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            throw Exception("All fields are required")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw Exception("Please enter a valid email address")
        }
        if (password.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }
        if (name.length < 2) {
            throw Exception("Name must be at least 2 characters")
        }
        
        // For demo purposes, we'll simulate registration since reqres.in register 
        // endpoint has API key requirements. We'll create a local user account.
        
        // Check if user already exists locally
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            throw Exception("User with this email already exists")
        }
        
        // Simulate successful registration by creating a local user
        // In a real app, this would make an actual API call to your backend
        val user = UserEntity(
            id = "demo_user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            token = "demo_token_${System.currentTimeMillis()}"
        )
        
        // Clear any existing user and store the new one
        userDao.clear()
        userDao.upsert(user)
        
        // Optional: You can still try to get user data from reqres.in for demo
        try {
            val usersResponse = authService.getUsers(page = 1)
            val demoUser = usersResponse.data.firstOrNull()
            
            if (demoUser != null) {
                // Update with real demo data from reqres.in
                val updatedUser = user.copy(
                    name = "${demoUser.first_name} ${demoUser.last_name}",
                    id = "demo_${demoUser.id}"
                )
                userDao.upsert(updatedUser)
            }
        } catch (e: Exception) {
            // Ignore errors when fetching demo data, registration already succeeded
        }
    }
    
    suspend fun getCurrentUser(): UserEntity? {
        return userDao.observeCurrentUser().firstOrNull()
    }
    
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
    
    suspend fun logout(): Result<Unit> = runCatching {
        userDao.clear()
    }
}


