package com.example.furniturestore.di

import com.example.furniturestore.data.local.AppDatabase
import com.example.furniturestore.data.remote.FurnitureApiService
import com.example.furniturestore.data.repository.FurnitureRepository
import com.example.furniturestore.data.repository.FurnitureRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFurnitureRepository(
        api: FurnitureApiService,
        db: AppDatabase
    ): FurnitureRepository = FurnitureRepositoryImpl(api, db)
}
