package com.example.amanotes.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Sync service for Firestore
 * 
 * Note: Firestore handles offline persistence and automatic sync automatically!
 * This service mainly monitors network state for UI purposes.
 * Firestore will automatically:
 * - Cache data locally when offline
 * - Sync changes when network becomes available
 * - Handle conflicts automatically
 */
class SyncServiceFirestore(private val context: Context) {
    
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    private val _isOnline = MutableStateFlow(isNetworkAvailable())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()
    
    private val _syncInProgress = MutableStateFlow(false)
    val syncInProgress: StateFlow<Boolean> = _syncInProgress.asStateFlow()
    
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isOnline.value = true
            // Firestore will automatically sync when network is available
            // No manual sync needed!
        }
        
        override fun onLost(network: Network) {
            _isOnline.value = false
            // Firestore will continue to work offline using cached data
        }
    }
    
    init {
        registerNetworkCallback()
    }
    
    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
    
    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    
    /**
     * Firestore syncs automatically, but you can use this to check sync status
     * or trigger UI updates
     */
    suspend fun checkSyncStatus() {
        // Firestore handles sync automatically
        // This is just for UI state management if needed
        _syncInProgress.value = !_isOnline.value
    }
    
    fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

