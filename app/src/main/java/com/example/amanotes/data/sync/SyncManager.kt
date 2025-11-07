package com.example.amanotes.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object SyncManager {
    private const val SYNC_WORK_NAME = "amanotes_sync_work"
    
    /**
     * Start periodic sync check
     * 
     * Note: Firestore handles actual sync automatically.
     * This is mainly for monitoring network status.
     */
    fun startPeriodicSync(context: Context) {
        // Firestore syncs automatically, so we don't need frequent sync checks
        // This is optional - you can remove if not needed
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        
        val syncWork = PeriodicWorkRequestBuilder<SyncWorker>(
            30, TimeUnit.MINUTES // Less frequent since Firestore handles it
        )
            .setConstraints(constraints)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncWork
        )
    }
    
    fun stopPeriodicSync(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(SYNC_WORK_NAME)
    }
    
    fun triggerSync(context: Context) {
        // Firestore syncs automatically, but you can trigger a status check
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val syncWork = androidx.work.OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()
        
        WorkManager.getInstance(context).enqueue(syncWork)
    }
}

