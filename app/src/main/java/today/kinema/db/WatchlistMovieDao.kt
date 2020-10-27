package today.kinema.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface WatchlistMovieDao {

    @Query(
        "SELECT * FROM watchlistMovie ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN dateTitle END ASC, " +
                "CASE WHEN :isAsc = 0 THEN dateTitle END DESC"
    )
    fun getWatchlistMovies(isAsc: Boolean): LiveData<List<WatchlistMovie>>

    @Query("SELECT * FROM watchlistMovie WHERE id = :id and dateTitle = :dateTitle")
    fun getWatchlistMovie(id: Int, dateTitle: String): LiveData<WatchlistMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchlistMovie: WatchlistMovie)

    @Delete
    suspend fun delete(item: WatchlistMovie)


}