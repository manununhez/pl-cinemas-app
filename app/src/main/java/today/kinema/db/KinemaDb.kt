package today.kinema.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main database description.
 */
@Database(
    entities = [WatchlistMovie::class],
    version = 1,
    exportSchema = false
)
abstract class KinemaDb : RoomDatabase() {

    abstract fun watchlistMovieDao(): WatchlistMovieDao
}