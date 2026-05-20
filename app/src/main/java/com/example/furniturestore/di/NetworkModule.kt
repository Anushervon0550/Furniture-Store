package com.example.furniturestore.di

import com.example.furniturestore.data.remote.FakeFurnitureApiService
import com.example.furniturestore.data.remote.FurnitureApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFurnitureApiService(): FurnitureApiService {
        // Используем фейковую реализацию как источник данных по умолчанию
        return FakeFurnitureApiService()
    }
}
