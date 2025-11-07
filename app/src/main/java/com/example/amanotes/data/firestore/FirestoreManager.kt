package com.example.amanotes.data.firestore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

object FirestoreManager {
    private var firestore: FirebaseFirestore? = null
    
    fun initialize(context: Context, enableOfflinePersistence: Boolean = true) {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance()
            
            // Configure Firestore settings
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(enableOfflinePersistence) // Enable offline persistence
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build()
            
            firestore?.firestoreSettings = settings
        }
    }
    
    fun getFirestore(): FirebaseFirestore {
        return firestore ?: throw IllegalStateException("Firestore not initialized. Call initialize() first.")
    }
}

