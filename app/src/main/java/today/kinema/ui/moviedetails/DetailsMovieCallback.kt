package today.kinema.ui.moviedetails

import today.kinema.vo.Movies

interface DetailsMovieCallback {
    fun watchTrailer()
    fun expandCollapseDescription()
    fun setWatchlist(movies: Movies)
}