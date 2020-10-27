package today.kinema.vo

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    val cinemaPageUrl: String,
    var distance: Float = 0.0f
) : Parcelable