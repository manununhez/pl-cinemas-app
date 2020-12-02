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
    private lateinit var moviesArg: Movie
    private lateinit var cinemaListAdapter: CinemasListAdapter
    private var isCurrentMovieInWatchlist: Boolean = false

    //SharedViewModel
    private val viewModelShared: MovieDetailsViewModel by viewModels()
    private val params by navArgs<MovieDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsMovieBinding.inflate(inflater, container, false).apply {
            toolbar.apply {
                setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
                title = resources.getString(R.string.movie_details_title)
            }
        }

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesArg = params.movie
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
                    val watchlistMovie = WatchlistMovie(movie)
                    if (isCurrentMovieInWatchlist) {
                        viewModelShared.onRemoveWatchlistBtnClicked(watchlistMovie)
                    } else {
                        viewModelShared.onAddWatchlistBtnClicked(watchlistMovie)
                    }
                }
            }

            movieDescription.post {
                if (movieDescription.lineCount <= resources.getInteger(R.integer.max_lines_collapsed))
                    expandCollapseOption.visibility = View.GONE
            }
        }


        reOrderMovieCinemasByDistance(moviesArg.cinemas)
        refreshWatchlistMovie()
        setupObservers()

        initRecyclerView()

    }

    private fun refreshWatchlistMovie() {
        viewModelShared.refreshWatchlist(WatchlistMovie(moviesArg))
    }

    private fun reOrderMovieCinemasByDistance(cinemas: List<Cinema>) {
        viewModelShared.orderCinemasByDistance(cinemas)
    }

    private fun setupObservers() {
        viewModelShared.cinemas.observe(viewLifecycleOwner) {
            cinemaListAdapter.submitList(it)
        }

        viewModelShared.watchlist.observe(viewLifecycleOwner) {
            isCurrentMovieInWatchlist = it
            binding.textWatchlist.apply {
                text = if (it) {
                    setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_watchlist, 0, 0)
                    resources.getString(R.string.watchlist_added)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_watchlist_add, 0, 0)
                    resources.getString(R.string.menu_item_watchlist)
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

        cinemaListAdapter = CinemasListAdapter(cinemaClickCallback)
        binding.rvCinemaList.adapter = cinemaListAdapter
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