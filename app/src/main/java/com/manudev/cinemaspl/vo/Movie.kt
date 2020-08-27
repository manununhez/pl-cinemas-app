package com.manudev.cinemaspl.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

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
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("logo_url")
    val logoUrl: String,
    @SerializedName("cinema_movie_url")
    val cinemaPageUrl:String
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