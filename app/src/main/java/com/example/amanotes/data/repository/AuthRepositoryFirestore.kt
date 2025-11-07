package com.example.amanotes.data.repository

import android.content.Context
import android.content.Intent
import com.example.amanotes.data.firestore.FirestoreManager
import com.example.amanotes.data.firestore.User
import com.example.amanotes.data.remote.GoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryFirestore(private val context: Context) {
    
    private val firestore = FirestoreManager.getFirestore()
    private val usersCollection = firestore.collection("users")
    private val googleSignInService = GoogleSignInService(context)
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: Flow<User?> = _currentUser.asStateFlow()
    
    init {
        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                // Fetch user from Firestore
                usersCollection.document(firebaseUser.uid)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            _currentUser.value = null
                            return@addSnapshotListener
                        }
                        val user = snapshot?.toObject(User::class.java)?.apply { id = snapshot.id }
                        _currentUser.value = user
                    }
            } else {
                _currentUser.value = null
            }
        }
    }
    
    fun getSignInIntent(): Intent {
        return googleSignInService.getSignInIntent()
    }
    
    suspend fun signInWithGoogle(data: Intent?): Result<User> = runCatching {
        // Step 1: Get Google account
        val account = googleSignInService.signInWithGoogle(data).getOrThrow()
        
        // Step 2: Authenticate with Firebase
        googleSignInService.authenticateWithFirebase(account).getOrThrow()
        
        // Step 3: Get Firebase user
        val firebaseUser = firebaseAuth.currentUser
            ?: throw Exception("Failed to get Firebase user")
        
        // Step 4: Store/Update user in Firestore
        val user = User(
            userId = firebaseUser.uid,
            name = account.displayName ?: firebaseUser.displayName ?: "",
            email = account.email ?: firebaseUser.email ?: "",
            photoUrl = account.photoUrl?.toString() ?: firebaseUser.photoUrl?.toString(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        // Check if user exists
        val existingUserQuery = usersCollection
            .whereEqualTo("userId", firebaseUser.uid)
            .limit(1)
            .get()
            .await()
        
        if (existingUserQuery.documents.isNotEmpty()) {
            // Update existing user
            val doc = existingUserQuery.documents.first()
            val existingUser = doc.toObject(User::class.java)?.apply {
                id = doc.id
            }
            
            val updatedUser = user.copy(
                id = existingUser?.id ?: doc.id,
                createdAt = existingUser?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            usersCollection.document(updatedUser.id)
                .set(updatedUser)
                .await()
            
            updatedUser
        } else {
            // Create new user
            val docRef = usersCollection.add(user).await()
            user.copy(id = docRef.id)
        }
    }
    
    suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return try {
            val query = usersCollection
                .whereEqualTo("userId", firebaseUser.uid)
                .limit(1)
                .get()
                .await()
            
            query.documents.firstOrNull()?.let { doc ->
                doc.toObject(User::class.java)?.apply {
                    id = doc.id
                }
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun logout(): Result<Unit> = runCatching {
        firebaseAuth.signOut()
        googleSignInService.signOut()
        _currentUser.value = null
    }
    
    suspend fun getUserByEmail(email: String): User? {
        return try {
            val query = usersCollection
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
            
            query.documents.firstOrNull()?.let { doc ->
                doc.toObject(User::class.java)?.apply {
                    id = doc.id
                }
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
    
    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Login failed.")
        
        // Get or create user in Firestore
        val existingUser = getUserByEmail(email)
        if (existingUser == null) {
            val user = User(
                id = firebaseUser.uid,
                userId = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString(),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            usersCollection.document(firebaseUser.uid).set(user).await()
        }
    }
    
    suspend fun signup(name: String, email: String, password: String): Result<Unit> = runCatching {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Signup failed.")
        
        // Create user profile in Firestore
        val user = User(
            id = firebaseUser.uid,
            userId = firebaseUser.uid,
            name = name,
            email = email,
            photoUrl = firebaseUser.photoUrl?.toString(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        usersCollection.document(firebaseUser.uid).set(user).await()
    }
}

