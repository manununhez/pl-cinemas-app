package today.kinema.util

import android.location.Location
import today.kinema.vo.Cinema
import today.kinema.vo.Coordinate
import today.kinema.vo.Movie

object LocationUtils {

    private fun getDistanceBetweenTwoPoints(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val distance = FloatArray(2)
        Location.distanceBetween(
            lat1, lon1,
            lat2, lon2, distance
        )
        return distance[0]
    }

    fun orderCinemasByDistance(
        currentLocation: Coordinate,
        movie: Movie
    ): Movie {
        if (currentLocation.isEmpty()) //LastKnownLocation not found!
            return movie

        //Order cinemas
        for (cinemaItem: Cinema in movie.cinemas) {
            cinemaItem.distance = getDistanceBetweenTwoPoints(
                currentLocation.latitude,
                currentLocation.longitude,
                cinemaItem.latitude.toDouble(),
                cinemaItem.longitude.toDouble()
            )
        }

        val cinemasSorted = movie.cinemas.sortedBy {
            it.distance
        }

        return Movie(movie, cinemasSorted)

    }


}