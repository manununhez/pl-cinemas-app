package today.kinema.data.db.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    primaryKeys = ["id", "dateTitle"],
    tableName = "watchlist_movies"
)
data class WatchlistMovie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date_title")
    val dateTitle: String,
    @SerializedName("movie")
    val movie: Movie,
    @SerializedName("title")
    val title: String
)