package com.manudev.cinemaspl.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    val SHARED_PREFERENCES_NAME = "prefs"

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.10:8000/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCinemaPLService(retrofit: Retrofit): CinemaPLService = retrofit.create(
        CinemaPLService::class.java
    )

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideMovieRepository(cinemaPLService: CinemaPLService, sharedPreferences: SharedPreferences) =
        MovieRepository(cinemaPLService, sharedPreferences)
}