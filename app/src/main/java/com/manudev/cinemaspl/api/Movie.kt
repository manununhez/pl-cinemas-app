package com.manudev.cinemaspl.api

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id")
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("trailer_url")
    val trailerUrl: String,
    @SerializedName("poster_url")
    val posterUrl: String
)

data class Cinema(
    @SerializedName("id")
    val id: String,
    val name: String,
    val website: String,
    @SerializedName("logo_url")
    val logoUrl: String
)

data class ResponseMovie(
    val success: Boolean,
    val data: List<Movies>
)

data class Movies(
    val movie: Movie,
    val cinemas: List<Cinema>
)