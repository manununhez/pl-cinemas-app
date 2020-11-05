package today.kinema.ui.moviedetails

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import today.kinema.R
import today.kinema.databinding.FragmentDetailsMovieBinding
import today.kinema.vo.Cinema
import today.kinema.vo.Movie
import today.kinema.vo.WatchlistMovie


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsMovieBinding
    private var savedWatchlistMovie: WatchlistMovie? = null

    //SharedViewModel
    private val viewModelShared: MovieDetailsViewModel by viewModels()

    private val params by navArgs<MovieDetailsFragmentArgs>()
    private lateinit var moviesArg: Movie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsMovieBinding.inflate(inflater, container, false).apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesArg = viewModelShared.orderCinemasByDistance(params.movie)
        binding.run {
            movie = moviesArg
            duration.text = if (moviesArg.duration == "0") "" else resources.getString(
                R.string.movie_duration,
                moviesArg.duration
            )

            detailsMovieCallback = object : DetailsMovieCallback {
                override fun watchTrailer() {
                    requireActivity().startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(moviesArg.trailerUrl)
                        )
                    )
                }

                override fun expandCollapseDescription() {
                    if (expandCollapseOption.text == resources.getString(R.string.description_text_collapsed)) {
                        expandCollapseOption.text =
                            resources.getString(R.string.description_text_expanded)
                        movieDescription.maxLines = Integer.MAX_VALUE
                        movieDescription.ellipsize = null
                    } else {
                        expandCollapseOption.text =
                            resources.getString(R.string.description_text_collapsed)
                        movieDescription.maxLines =
                            resources.getInteger(R.integer.max_lines_collapsed)
                        movieDescription.ellipsize = TextUtils.TruncateAt.END
                    }
                }

                override fun setWatchlist(movie: Movie) {
                    val favoriteMovie = WatchlistMovie(movie)
                    if (savedWatchlistMovie == null) {
                        viewModelShared.addWatchlistMovie(favoriteMovie)
                    } else {
                        viewModelShared.removeWatchlistMovie(favoriteMovie)
                    }
                }
            }

            movieDescription.post {
                if (movieDescription.lineCount <= resources.getInteger(R.integer.max_lines_collapsed))
                    expandCollapseOption.visibility = View.GONE
            }
        }

        getWatchlistMovie()
        initRecyclerView()

    }

    private fun getWatchlistMovie() {
        viewModelShared.getWatchlistMovie(WatchlistMovie(moviesArg))
        viewModelShared.watchlist.observe(viewLifecycleOwner) {
            savedWatchlistMovie = it
            binding.textWatchlist.apply {
                text = if (it == null) {
                    setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_watchlist_add, 0, 0)
                    resources.getString(R.string.menu_item_watchlist)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_watchlist, 0, 0)
                    resources.getString(R.string.watchlist_added)
                }
            }
        }

    }

    private fun initRecyclerView() {
        val cinemaClickCallback = object :
            CinemaViewClickCallback {
            override fun onClick(cinema: Cinema) {
                openCinemaPageURL(cinema)
            }
        }

        binding.rvCinemaList.adapter =
            CinemaMovieListAdapter(moviesArg.cinemas, cinemaClickCallback)
    }

    private fun openCinemaPageURL(cinema: Cinema) {
        requireActivity().startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(cinema.cinemaPageUrl)
            )
        )
    }
}