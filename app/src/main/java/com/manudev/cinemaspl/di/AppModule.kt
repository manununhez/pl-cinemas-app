package com.manudev.cinemaspl.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.10:8000/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCinemaPLService(retrofit: Retrofit): CinemaPLService = retrofit.create(
        CinemaPLService::class.java)


    @Singleton
    @Provides
    fun provideMovieRepository(cinemaPLService: CinemaPLService) =
        MovieRepository(cinemaPLService)
}