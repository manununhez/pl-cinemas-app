package today.kinema.ui.movie

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import today.kinema.R
import today.kinema.data.api.Status
import today.kinema.databinding.FragmentMovieBinding
import today.kinema.ui.common.RetryCallback
import today.kinema.vo.Attribute
import today.kinema.vo.Movie

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private lateinit var attributes: Attribute
    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var daysListAdapter: DaysListAdapter

    //sharedViewModel - navGraph scope
    private val viewModelShared: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }

        returnTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieBinding.inflate(inflater, container, false)
        binding.toolbar.setOnMenuItemClickListener(this)

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.move)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Postpone enter transitions to allow shared element transitions to run.
        // https://github.com/googlesamples/android-architecture-components/issues/495
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.run {
            lifecycleOwner = viewLifecycleOwner
            movie = viewModelShared.movies
            retryCallback = object : RetryCallback {
                override fun retry() {
                    viewModelShared.retry()
                }
            }
        }

        updateWatchlist()
        updateMovies()
        initMoviesRecyclerView()
        initDateTitleRecyclerView()

        setupObservers()
    }

    private fun updateWatchlist(){
        viewModelShared.updateWatchlist()
    }

    private fun updateMovies() {
        viewModelShared.updateMovies()
    }

    private fun initMoviesRecyclerView() {
        val movieClickCallback = object : MovieViewClickCallback {
            override fun onClick(view: View, movies: Movie) {
                navigateToMovieDetailsFragment(view, movies)
            }
        }

        movieListAdapter = MovieListAdapter(movieClickCallback, viewModelShared.watchlist, viewLifecycleOwner)

        binding.movieListGrid.adapter = movieListAdapter
    }

    private fun initDateTitleRecyclerView() {
        val dayTitleClickCallback = object : DayTitleViewClickCallback {
            override fun onClick(cardView: View, dateTitle: String) {
                viewModelShared.setDateMoviesTitle(dateTitle)
            }
        }

        daysListAdapter =
            DaysListAdapter(
                dayTitleClickCallback,
                viewModelShared.currentFilterAttribute,
                viewLifecycleOwner
            )

        binding.rvDaysTitle.adapter = daysListAdapter
    }

    private fun setupObservers() {
        viewModelShared.movies.observe(viewLifecycleOwner, {
            it?.let {
                if (it.status == Status.SUCCESS) {
//                    Timber.d(it.toString())
                    movieListAdapter.submitList(it.data)
                } else if (it.status == Status.ERROR) {
                    movieListAdapter.submitList(listOf())
                }
            }
        })

        viewModelShared.attributes.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                if (it.data != null) {
                    attributes = it.data
                    daysListAdapter.submitList(it.data.days)

                    //scroll rv to selected date
                    binding.rvDaysTitle.scrollToPosition(
                        daysListAdapter.currentList.indexOf(
                            viewModelShared.currentFilterAttribute.value?.date
                        )
                    )
                }
            }
        })

        viewModelShared.currentFilterAttribute.observe(viewLifecycleOwner, {
            binding.tvSubtitle.text = if (it.city.isNotEmpty()) it.city else ""
        })
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.filterFragmentMenu -> navigateToFilterFragment()
            R.id.watchlistFragmentMenu -> navigateToWatchListFragment()
            R.id.sortMoviesMenu -> viewModelShared.updateMovieListOrder()
        }
        return true
    }

    private fun navigateToMovieDetailsFragment(view: View, movie: Movie) {
        // Set exit and reenter transitions here as opposed to in onCreate because these transitions
        // will be set and overwritten on HomeFragment for other navigation actions.
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val movieDetailTransitionName =
                resources.getString(R.string.movie_detail_transition_name)

            val extras = FragmentNavigatorExtras(
                view to movieDetailTransitionName
            )

            val action = MovieFragmentDirections.showDetailsMovie(movie)

            findNavController().navigate(action, extras)
        } else {
            findNavController().navigate(MovieFragmentDirections.showDetailsMovie(movie))
        }
    }

    private fun navigateToFilterFragment() {
        // Set exit and reenter transitions here as opposed to in onCreate because these transitions
        // will be set and overwritten on HomeFragment for other navigation actions.
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }

        val directions = MovieFragmentDirections.showFilterFragment(
            attributes
        )
        findNavController().navigate(directions)
    }

    private fun navigateToWatchListFragment() {
        // Set exit and reenter transitions here as opposed to in onCreate because these transitions
        // will be set and overwritten on HomeFragment for other navigation actions.
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }

        val directions = MovieFragmentDirections.showWatchlistFragment()
        findNavController().navigate(directions)
    }
}