package com.example.furniturestore.di

import android.content.Context
import com.example.furniturestore.data.local.preferences.TokenManager
import com.example.furniturestore.data.remote.AuthService
import com.example.furniturestore.data.remote.FakeAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FakeAuthService()

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)
}
