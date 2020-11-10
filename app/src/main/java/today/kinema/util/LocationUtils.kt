package today.kinema.util

import android.location.Location
import today.kinema.vo.Cinema
import today.kinema.vo.Coordinate

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
        cinemas: List<Cinema>
    ): List<Cinema> {
        if (currentLocation.isEmpty()) //LastKnownLocation not found!
            return cinemas

        //Order cinemas
        for (cinemaItem: Cinema in cinemas) {
            cinemaItem.distance = getDistanceBetweenTwoPoints(
                currentLocation.latitude,
                currentLocation.longitude,
                cinemaItem.latitude.toDouble(),
                cinemaItem.longitude.toDouble()
            )
        }

        return cinemas.sortedBy {
            it.distance
        }
    }


}