package com.hilmi.wartego.di

import com.hilmi.wartego.data.datasource.auth.FirebaseAuthDataSourceImpl
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsFirebaseAuthRepository(firebaseAuthDataSourceImpl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository
}