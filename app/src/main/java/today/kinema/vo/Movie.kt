package today.kinema.vo

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
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
    val posterUrl: String
) : Parcelable


