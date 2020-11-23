package today.kinema.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import today.kinema.data.api.KinemaDataSource
import today.kinema.data.db.RoomDataSource
import today.kinema.repository.KinemaRepository
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideKinemaRepository(
        kinemaDataSource: KinemaDataSource,
        roomDataSource: RoomDataSource
    ) =
        KinemaRepository(kinemaDataSource, roomDataSource)

    /**
     * Since all of our dispatchers share the same type, CoroutineDispatcher,  we need
     * to help Dagger to distinguish between them and @Qualifier is an ideal Dagger construct for that.
     */
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}