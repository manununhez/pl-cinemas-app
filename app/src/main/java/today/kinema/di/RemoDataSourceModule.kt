package today.kinema.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import today.kinema.data.api.KinemaApi
import today.kinema.data.api.RemoteDataSourceImpl
import today.kinema.data.api.KinemaService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RemoDataSourceModule {
    @Singleton
    @Provides
    fun provideKinemaApi(gson: Gson) = KinemaApi(gson)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideKinemaService(kinemaApi: KinemaApi): KinemaService = kinemaApi.service.create(
        KinemaService::class.java
    )

    @Provides
    fun provideKinemaDataSource(kinemaService: KinemaService): RemoteDataSourceImpl =
        RemoteDataSourceImpl(kinemaService)
}