package today.kinema.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.TypeConverters
import today.kinema.vo.Movies

@Entity(primaryKeys = ["id", "dateTitle"])
@TypeConverters(KinemaTypeConverters::class)
data class WatchlistMovie(
    var id: Int,
    var dateTitle: String,
    var movies: Movies,
    var header: Boolean
) {
    @Ignore
    constructor(movies: Movies) : this(
        Integer.parseInt(movies.movie.id),
        movies.movie.dateTitle,
        movies,
        false
    )
}