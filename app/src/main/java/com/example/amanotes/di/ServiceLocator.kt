package com.example.amanotes.di

import android.content.Context
import androidx.room.Room
import com.example.amanotes.BuildConfig
import com.example.amanotes.data.local.AppDatabase
import com.example.amanotes.data.remote.AuthService
import com.example.amanotes.data.repository.AuthRepository
import com.example.amanotes.data.repository.TaskRepository
import com.example.amanotes.data.repository.NoteRepository
import com.example.amanotes.data.repository.ProjectRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    @Volatile private var retrofit: Retrofit? = null
    @Volatile private var database: AppDatabase? = null

    fun provideRetrofit(): Retrofit {
        val existing = retrofit
        if (existing != null) return existing
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val instance = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit = instance
        return instance
    }

    fun provideAuthService(): AuthService = provideRetrofit().create(AuthService::class.java)

    fun provideDatabase(context: Context): AppDatabase {
        val existing = database
        if (existing != null) return existing
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "amanotes.db")
            .fallbackToDestructiveMigration()
            .build()
        database = db
        return db
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val authService = provideAuthService()
        val userDao = provideDatabase(context).userDao()
        return AuthRepository(authService, userDao)
    }

    fun provideTaskRepository(context: Context): TaskRepository {
        val taskDao = provideDatabase(context).taskDao()
        return TaskRepository(taskDao)
    }

    fun provideNoteRepository(context: Context): NoteRepository {
        val noteDao = provideDatabase(context).noteDao()
        return NoteRepository(noteDao)
    }

    fun provideProjectRepository(context: Context): ProjectRepository {
        val projectDao = provideDatabase(context).projectDao()
        return ProjectRepository(projectDao)
    }
}


