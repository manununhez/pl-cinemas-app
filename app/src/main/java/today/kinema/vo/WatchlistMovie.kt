package today.kinema.vo

data class WatchlistMovie(
    val id: Int,
    val dateTitle: String,
    val movie: Movie,
    var header: Boolean = false
) {
    constructor(movie: Movie) : this(
        Integer.parseInt(movie.id),
        movie.dateTitle,
        movie,
        false
    )
}