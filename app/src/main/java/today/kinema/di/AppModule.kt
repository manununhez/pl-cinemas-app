package today.kinema.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import today.kinema.data.api.KinemaApi
import today.kinema.data.api.KinemaDataSource
import today.kinema.data.api.KinemaService
import today.kinema.data.db.KinemaDb
import today.kinema.data.db.RoomDataSource
import today.kinema.data.db.WatchlistMovieDao
import today.kinema.repository.MovieRepository
import today.kinema.util.AppExecutors
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    private const val SHARED_PREFERENCES_NAME = "prefs"

    @Singleton
    @Provides
    fun provideKinemaApi(gson: Gson) = KinemaApi(gson)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideKinemaService(kinemaApi: KinemaApi): KinemaService = kinemaApi.service.create(
        KinemaService::class.java
    )

    @Provides
    fun provideKinemaDataSource(kinemaService: KinemaService): KinemaDataSource =
        KinemaDataSource(kinemaService)

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


    @Singleton
    @Provides
    fun provideMovieRepository(
        kinemaDataSource: KinemaDataSource,
        roomDataSource: RoomDataSource,
        appExecutors: AppExecutors
    ) =
        MovieRepository(kinemaDataSource, roomDataSource, appExecutors)

    @Singleton
    @Provides
    fun provideKinemaDb(app: Application) = KinemaDb.build(app)

    @Singleton
    @Provides
    fun provideFavoriteMovieDao(db: KinemaDb): WatchlistMovieDao {
        return db.watchlistMovieDao()
    }

    @Provides
    fun provideLocalStorage(sharedPreferences: SharedPreferences, gson: Gson, db: KinemaDb) =
        RoomDataSource(sharedPreferences, gson, db)
}