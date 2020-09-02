package com.manudev.cinemaspl.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Movie(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("classification")
    val classification: String,
    @SerializedName("release_year")
    val releaseYear: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("trailer_url")
    val trailerUrl: String,
    @SerializedName("poster_url")
    val posterUrl: String
) : Parcelable

@Parcelize
data class Cinema(
    @SerializedName("cinema_id")
    val cinemaId: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("coord_latitude")
    val latitude: String,
    @SerializedName("coord_longitude")
    val longitude: String,
    @SerializedName("logo_url")
    val logoUrl: String,
    @SerializedName("cinema_movie_url")
    val cinemaPageUrl: String
) : Parcelable

@Parcelize
data class GeneralResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: @RawValue T
) : Parcelable

@Parcelize
data class Movies(
    @SerializedName("movie")
    val movie: Movie,
    @SerializedName("cinemas")
    val cinemas: List<Cinema>
) : Parcelable

@Parcelize
data class Location(
    @SerializedName("city")
    val city: String
) : Parcelable

//To pass as a parcelable navArgs
@Parcelize
data class Locations(
    val locations: List<Location>
) : Parcelable

@Parcelize
data class DayTitle(
    @SerializedName("date")
    val date: String
) : Parcelable {
    companion object {
        fun dateFormat(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }
}