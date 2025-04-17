package com.hilmi.wartego.di

import com.hilmi.wartego.data.datasource.auth.FirebaseAuthDataSource
import com.hilmi.wartego.data.datasource.auth.FirebaseAuthDataSourceImpl
import com.hilmi.wartego.data.datasource.dashboard.FirebaseProductDataSource
import com.hilmi.wartego.data.datasource.dashboard.FirebaseProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindsFirebaseAuthDataSource(firebaseDataSourceImpl: FirebaseAuthDataSourceImpl): FirebaseAuthDataSource

    @Binds
    abstract fun bindsFirebaseProductDataSource(firebaseDataSourceImpl:FirebaseProductDataSourceImpl): FirebaseProductDataSource
}