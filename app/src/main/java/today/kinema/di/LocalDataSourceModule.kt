package today.kinema.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import today.kinema.data.db.KinemaDb
import today.kinema.data.db.LocalDataSourceImpl
import today.kinema.data.db.SharedPreferencesDB
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object LocalDataSourceModule {
    private const val SHARED_PREFERENCES_NAME = "prefs"

    @Singleton
    @Provides
    fun provideKinemaDb(app: Application) = KinemaDb.build(app)

    @Provides
    fun provideRoomDataSource(sharedPreferencesDB: SharedPreferencesDB, db: KinemaDb) =
        LocalDataSourceImpl(sharedPreferencesDB, db.watchlistMovieDao(), db.movieDao())

    @Provides
    fun provideSharedPreferencesDB(sharedPreferences: SharedPreferences, gson: Gson) =
        SharedPreferencesDB(sharedPreferences, gson)

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }
}