package today.kinema.data.db.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    primaryKeys = ["id"],
    tableName = "movies",
)
data class Movie(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("original_lang")
    val originalLanguage: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("classification")
    val classification: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("release_year")
    val releaseYear: String,
    @SerializedName("date_title")
    val dateTitle: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("trailer_url")
    val trailerUrl: String,
    @SerializedName("poster_url")
    val posterUrl: String,
    @SerializedName("cinemas")
    val cinemas: List<Cinema>
)

@Entity
data class Cinema(
    @SerializedName("cinema_id")
    val cinemaId: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val locationName: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("coord_latitude")
    val latitude: String,
    @SerializedName("coord_longitude")
    val longitude: String,
    @SerializedName("logo_url")
    val logoUrl: String,
    @SerializedName("cinema_movie_url")
    val cinemaPageUrl: String
)



