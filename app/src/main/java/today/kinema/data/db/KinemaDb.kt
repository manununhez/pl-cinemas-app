package today.kinema.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import today.kinema.data.db.model.WatchlistMovie

/**
 * Main database description.
 */

private const val DATABASE_NAME = "kinema.db"

@Database(
    entities = [WatchlistMovie::class],
    version = 1,
    exportSchema = false
)
abstract class KinemaDb : RoomDatabase() {

    companion object {
        fun build(context: Context) = Room
            .databaseBuilder(context, KinemaDb::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    abstract fun watchlistMovieDao(): WatchlistMovieDao
}