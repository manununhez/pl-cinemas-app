package today.kinema.vo

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
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
    val cinemaPageUrl: String
) : Parcelable {
    /**
     * Note that the compiler only uses the properties defined inside the primary constructor for the automatically generated functions.
     * To exclude a property from the generated implementations, declare it inside the class body.
     */
    @IgnoredOnParcel
    var distance: Float = 0.0f
}


