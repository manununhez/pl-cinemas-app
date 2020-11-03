package today.kinema.data

import today.kinema.data.api.model.Attribute as ServerAttribute
import today.kinema.data.api.model.Cinema as ServerCinema
import today.kinema.data.api.model.FilterAttribute as ServerFilterAttribute
import today.kinema.data.api.model.Movie as ServerMovie
import today.kinema.data.db.model.Attribute as RoomAttribute
import today.kinema.data.db.model.Cinema as RoomCinema
import today.kinema.data.db.model.Coordinate as RoomCoordinate
import today.kinema.data.db.model.FilterAttribute as RoomFilterAttribute
import today.kinema.data.db.model.Movie as RoomMovie
import today.kinema.data.db.model.WatchlistMovie as RoomWatchlistMovie
import today.kinema.vo.Attribute as DomainAttribute
import today.kinema.vo.Cinema as DomainCinema
import today.kinema.vo.Coordinate as DomainCoordinate
import today.kinema.vo.FilterAttribute as DomainFilterAttribute
import today.kinema.vo.Movie as DomainMovie
import today.kinema.vo.WatchlistMovie as DomainWatchlistMovie

fun RoomWatchlistMovie.toDomainWatchlistMovie(): DomainWatchlistMovie =
    DomainWatchlistMovie(
        id,
        dateTitle,
        movie.toDomainMovie()
    )

fun DomainWatchlistMovie.toRoomWatchlistMovie(): RoomWatchlistMovie =
    RoomWatchlistMovie(
        id,
        dateTitle,
        movie.toRoomMovie(),
        movie.title
    )

fun DomainMovie.toRoomMovie(): RoomMovie =
    RoomMovie(
        id,
        title,
        description,
        originalLanguage,
        duration,
        classification,
        genre,
        releaseYear,
        dateTitle,
        city,
        trailerUrl,
        posterUrl,
        cinemas.map {
            it.toRoomCinema()
        }
    )

fun RoomMovie.toDomainMovie(): DomainMovie =
    DomainMovie(
        id,
        title,
        description,
        originalLanguage,
        duration,
        classification,
        genre,
        releaseYear,
        dateTitle,
        city,
        trailerUrl,
        posterUrl,
        cinemas.map {
            it.toDomainCinema()
        }
    )

fun ServerMovie.toDomainMovie(): DomainMovie =
    DomainMovie(
        id,
        title,
        description,
        originalLanguage,
        duration,
        classification,
        genre,
        releaseYear,
        dateTitle,
        city,
        trailerUrl,
        posterUrl,
        cinemas.map {
            it.toDomainCinema()
        }
    )

fun DomainCinema.toRoomCinema(): RoomCinema =
    RoomCinema(
        cinemaId,
        locationId,
        locationName,
        language,
        latitude,
        longitude,
        logoUrl,
        cinemaPageUrl
    )

fun RoomCinema.toDomainCinema(): DomainCinema =
    DomainCinema(
        cinemaId,
        locationId,
        locationName,
        language,
        latitude,
        longitude,
        logoUrl,
        cinemaPageUrl
    )

fun ServerCinema.toDomainCinema(): DomainCinema =
    DomainCinema(
        cinemaId,
        locationId,
        locationName,
        language,
        latitude,
        longitude,
        logoUrl,
        cinemaPageUrl
    )

fun ServerAttribute.toRoomAttribute(): RoomAttribute =
    RoomAttribute(
        cinemas,
        cities,
        days,
        languages
    )

fun RoomAttribute.toDomainAttribute(): DomainAttribute =
    DomainAttribute(
        cinemas,
        cities,
        days,
        languages
    )

fun DomainFilterAttribute.toRoomFilterAttribute(): RoomFilterAttribute =
    RoomFilterAttribute(
        city, date, cinema, language
    )

fun DomainFilterAttribute.toServerFilterAttribute(): ServerFilterAttribute =
    ServerFilterAttribute(
        city, date, cinema, language
    )

fun RoomFilterAttribute.toDomainFilterAttribute(): DomainFilterAttribute =
    DomainFilterAttribute(
        city, date, cinema, language
    )

fun RoomCoordinate.toDomainCoordinate(): DomainCoordinate =
    DomainCoordinate(latitude, longitude)
