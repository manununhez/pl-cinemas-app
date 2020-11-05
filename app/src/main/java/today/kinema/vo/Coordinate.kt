package today.kinema.vo

data class Coordinate(
    val latitude: Double,
    val longitude: Double
) {
    fun isEmpty(): Boolean {
        return latitude == 0.0 && longitude == 0.0
    }
}