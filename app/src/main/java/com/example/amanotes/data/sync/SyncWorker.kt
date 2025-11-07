package com.example.amanotes.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Sync worker for Firestore
 * 
 * Note: Firestore handles sync automatically, so this worker
 * mainly checks network status. The actual sync is handled
 * by Firestore's built-in offline persistence.
 */
class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Firestore syncs automatically - just check status
            val syncService = SyncServiceFirestore(applicationContext)
            syncService.checkSyncStatus()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

