package today.kinema.data.db.model

import androidx.room.Entity
import androidx.room.TypeConverters
import today.kinema.data.db.KinemaTypeConverters
import today.kinema.vo.Movie

@Entity(primaryKeys = ["id", "dateTitle"])
@TypeConverters(KinemaTypeConverters::class)
data class WatchlistMovie(
    val id: Int,
    val dateTitle: String,
    val movie: Movie,
    val title: String
)