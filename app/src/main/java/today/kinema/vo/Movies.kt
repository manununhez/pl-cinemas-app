package today.kinema.vo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import today.kinema.db.KinemaTypeConverters

@Entity
@Parcelize
data class Movies(
    @SerializedName("movie")
    val movie: Movie,
    @SerializedName("cinemas")
    val cinemas: List<Cinema>
) : Parcelable