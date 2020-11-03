package today.kinema.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import today.kinema.data.db.model.Movie


@Dao
interface MovieDao {

    @Query(
        "SELECT * FROM movies ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN title END ASC, " +
                "CASE WHEN :isAsc = 0 THEN title END DESC"
    )
    suspend fun getMovies(isAsc: Boolean): List<Movie>

    @Query("SELECT * FROM movies LIMIT 1")
    suspend fun getFirstMovie(): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    suspend fun clear()


}