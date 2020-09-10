package com.manudev.cinemaspl.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("classification")
    val classification: String,
    @SerializedName("release_year")
    val releaseYear: String,
    @SerializedName("date_title")
    val dateTitle: String,
    @SerializedName("trailer_url")
    val trailerUrl: String,
    @SerializedName("poster_url")
    val posterUrl: String
) : Parcelable

@Parcelize
data class Movies(
    @SerializedName("movie")
    val movie: Movie,
    @SerializedName("cinemas")
    val cinemas: List<Cinema>
) : Parcelable


