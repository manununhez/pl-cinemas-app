package today.kinema.vo

data class WatchlistMovie(
    val id: Int,
    val dateTitle: String,
    val movie: Movie,
    val title: String
) {
    constructor(movie: Movie) : this(
        Integer.parseInt(movie.id),
        movie.dateTitle,
        movie,
        movie.title
    )

    var header: Boolean = false
}