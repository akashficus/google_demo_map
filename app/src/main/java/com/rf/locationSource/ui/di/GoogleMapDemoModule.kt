package com.rf.locationSource.ui.di

import android.content.Context
import com.rf.locationSource.ui.base.BaseViewModelFactory
import com.rf.locationSource.data.repository.GoogleMapLocationRepository
import com.rf.locationSource.data.repository.remote.GoogleMapDemoApiServices
import com.rf.locationSource.localDB.GoogleMapDemoDB
import com.rf.locationSource.ui.viewmodel.GoogleMapDemoViewModel
import com.rf.locationSource.utils.SharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class) // Singleton scope for global usage
object GoogleMapDemoModule {

    @Provides
    fun provideGoogleMapDemoApiServices(retrofit: Retrofit): GoogleMapDemoApiServices =
        retrofit.create(GoogleMapDemoApiServices::class.java)

    @Provides
    fun provideGoogleMapDemoRepository(
        apiServices: GoogleMapDemoApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        googleMapDemoDB: GoogleMapDemoDB
    ): GoogleMapLocationRepository =
        GoogleMapLocationRepository(apiServices, context, sharedPreference, googleMapDemoDB)

    @Provides
    fun provideViewModelFactory(googleMapLocationRepository: GoogleMapLocationRepository): BaseViewModelFactory<GoogleMapDemoViewModel> =
        BaseViewModelFactory { GoogleMapDemoViewModel(googleMapLocationRepository) }
}