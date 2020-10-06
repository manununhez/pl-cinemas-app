package today.kinema.util

import android.location.Location
import today.kinema.vo.Cinema
import today.kinema.vo.Coordinate
import today.kinema.vo.Movies

object LocationUtils {

    fun getDistanceBetweenTwoPoints(
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
        moviesList: List<Movies>
    ): List<Movies> {
        if (currentLocation.latitude == 0.0 && currentLocation.longitude == 0.0) //LastKnownLocation not found!
            return moviesList

        val tmpMoviesList = mutableListOf<Movies>()
        for (movieItem: Movies in moviesList) {
            //Order cinemas
            for (cinemaItem: Cinema in movieItem.cinemas) {
                cinemaItem.distance = getDistanceBetweenTwoPoints(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    cinemaItem.latitude.toDouble(),
                    cinemaItem.longitude.toDouble()
                )
            }

            val cinemasSorted = movieItem.cinemas.sortedBy {
                it.distance
            }

            tmpMoviesList.add(Movies(movieItem.movie, cinemasSorted))

        }

        return tmpMoviesList.toList()

    }


}