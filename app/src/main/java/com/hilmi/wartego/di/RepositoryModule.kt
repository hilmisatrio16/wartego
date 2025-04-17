package com.hilmi.wartego.di

import com.hilmi.wartego.data.datasource.auth.FirebaseAuthDataSourceImpl
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepositoryImpl
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsFirebaseAuthRepository(firebaseAuthRepositoryImpl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository

    @Binds
    abstract fun bindsFirebaseProductRepository(firebaseProductRepositoryImpl: FirebaseProductRepositoryImpl): FirebaseProductRepository
}