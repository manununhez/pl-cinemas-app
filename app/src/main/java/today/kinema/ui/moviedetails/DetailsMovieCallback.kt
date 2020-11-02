package today.kinema.ui.moviedetails

import today.kinema.vo.Movie

interface DetailsMovieCallback {
    fun watchTrailer()
    fun expandCollapseDescription()
    fun setWatchlist(movie: Movie)
}