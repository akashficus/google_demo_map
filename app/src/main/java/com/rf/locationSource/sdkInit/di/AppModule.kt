package com.rf.locationSource.sdkInit.di

import android.content.Context
import com.rf.locationSource.localDB.GoogleMapDemoDB
import com.rf.locationSource.utils.AppConfig
import com.rf.locationSource.utils.NetworkHelper
import com.rf.locationSource.utils.SharedPreference
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAppConfig(@ApplicationContext context: Context): AppConfig = AppConfig(context)

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreference = SharedPreference(context)

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper = NetworkHelper(context)

    @Provides
    @Singleton
    fun provideSwipePosDatabase(@ApplicationContext context: Context): GoogleMapDemoDB = GoogleMapDemoDB.getDatabase(context)

    @Provides
    @Singleton
    fun provideRetrofit(appConfig: AppConfig): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl(appConfig.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

}