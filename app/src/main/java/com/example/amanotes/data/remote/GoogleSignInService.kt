package com.example.amanotes.data.remote

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleSignInService(private val context: Context) {
    
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    
    private val googleSignInClient: GoogleSignInClient by lazy {
        val webClientId = context.getString(com.example.amanotes.R.string.default_web_client_id)
        val gso = if (webClientId != "YOUR_WEB_CLIENT_ID_HERE") {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()
        } else {
            // Fallback for development - will need proper setup for production
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        }
        GoogleSignIn.getClient(context, gso)
    }
    
    fun getSignInIntent() = googleSignInClient.signInIntent
    
    suspend fun signInWithGoogle(data: Intent?): Result<GoogleSignInAccount> = runCatching {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        account
    }
    
    suspend fun authenticateWithFirebase(account: GoogleSignInAccount): Result<String> = runCatching {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        result.user?.getIdToken(true)?.await()?.token 
            ?: throw Exception("Failed to get Firebase token")
    }
    
    suspend fun signOut() {
        googleSignInClient.signOut().await()
        firebaseAuth.signOut()
    }
    
    fun getCurrentUser() = firebaseAuth.currentUser
}

