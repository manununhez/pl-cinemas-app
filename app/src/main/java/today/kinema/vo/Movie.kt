package today.kinema.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: String,
    val title: String,
    val description: String,
    val originalLanguage: String,
    val duration: String,
    val classification: String,
    val genre: String,
    val releaseYear: String,
    val dateTitle: String,
    val city: String,
    val trailerUrl: String,
    val posterUrl: String,
    val cinemas: List<Cinema>
) : Parcelable {
    constructor(movie: Movie, cinemas: List<Cinema>) : this(
        movie.id,
        movie.title,
        movie.description,
        movie.originalLanguage,
        movie.duration,
        movie.classification,
        movie.genre,
        movie.releaseYear,
        movie.dateTitle,
        movie.city,
        movie.trailerUrl,
        movie.posterUrl,
        cinemas
    )
}

@Parcelize
data class Cinema(
    val cinemaId: String,
    val locationId: String,
    val locationName: String,
    val language: String,
    val latitude: String,
    val longitude: String,
    val logoUrl: String,
    val cinemaPageUrl: String,
    var distance: Float = 0.0f
) : Parcelable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cinema

        if (cinemaId != other.cinemaId) return false
        if (locationId != other.locationId) return false
        if (locationName != other.locationName) return false
        if (language != other.language) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (logoUrl != other.logoUrl) return false
        if (cinemaPageUrl != other.cinemaPageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cinemaId.hashCode()
        result = 31 * result + locationId.hashCode()
        result = 31 * result + locationName.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + logoUrl.hashCode()
        result = 31 * result + cinemaPageUrl.hashCode()
        return result
    }
}


