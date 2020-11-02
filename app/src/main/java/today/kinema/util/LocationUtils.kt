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
        moviesList: List<Movie>
    ): List<Movie> {
        if (currentLocation.latitude == 0.0 && currentLocation.longitude == 0.0) //LastKnownLocation not found!
            return moviesList

        val tmpMovieList = mutableListOf<Movie>()
        for (movieItem: Movie in moviesList) {
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

            tmpMovieList.add(Movie(movieItem, cinemasSorted))

        }

        return tmpMovieList.toList()

    }


}