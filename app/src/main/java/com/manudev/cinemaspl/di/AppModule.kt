package com.manudev.cinemaspl.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.manudev.cinemaspl.api.CinemaPLService
import com.manudev.cinemaspl.db.LocalStorage
import com.manudev.cinemaspl.repository.MovieRepository
import com.manudev.cinemaspl.util.AppExecutors
import com.manudev.cinemaspl.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    private const val SHARED_PREFERENCES_NAME = "prefs"

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
//        .baseUrl("http://192.168.1.10:8000/api/") //development
        .baseUrl("https://kinema.today/api/") //production
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .client(//Added interceptor to print level body request/response Retrofit
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
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

    @Provides
    fun provideAppExecutors() = AppExecutors()

    @Provides
    fun provideLocalStorage(sharedPreferences: SharedPreferences) = LocalStorage(sharedPreferences)

    @Singleton
    @Provides
    fun provideMovieRepository(
        cinemaPLService: CinemaPLService,
        localStorage: LocalStorage,
        appExecutors: AppExecutors
    ) =
        MovieRepository(cinemaPLService, localStorage, appExecutors)
}