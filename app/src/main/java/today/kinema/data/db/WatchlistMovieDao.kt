package today.kinema.data.db

import androidx.room.*
import today.kinema.data.db.model.WatchlistMovie

@Dao
interface WatchlistMovieDao {

    @Query(
        "SELECT * FROM watchlist_movies ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN dateTitle END ASC, " +
                "CASE WHEN :isAsc = 0 THEN dateTitle END DESC," +
                "title"
    )
    suspend fun getWatchlistMovies(isAsc: Boolean): List<WatchlistMovie>

    @Query("SELECT count(*) FROM watchlist_movies WHERE id = :id and dateTitle = :dateTitle")
    suspend fun checkIfWatchMovieExists(id: Int, dateTitle: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchlistMovie: WatchlistMovie)

    @Delete
    suspend fun delete(item: WatchlistMovie)


}