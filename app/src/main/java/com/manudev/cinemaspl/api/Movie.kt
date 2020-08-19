package com.manudev.cinemaspl.api

import com.squareup.moshi.Json

data class Movie(
    @Json(name = "id")
    val id: String,
    val title: String,
    val description: String,
    @Json(name = "trailer_url")
    val trailerUrl: String,
    @Json(name = "poster_url")
    val posterUrl: String
)

data class Cinema(
    @Json(name = "id")
    val id: String,
    val name: String,
    val website: String,
    @Json(name = "logo_url")
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