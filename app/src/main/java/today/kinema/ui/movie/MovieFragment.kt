package today.kinema.ui.movie

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.transition.TransitionInflater
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import today.kinema.R
import today.kinema.databinding.FragmentMovieBinding
import today.kinema.ui.SharedMovieViewModel
import today.kinema.ui.common.RetryCallback
import today.kinema.vo.Attribute
import today.kinema.vo.Movies
import today.kinema.vo.Status

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    companion object {
        private val TAG: String? = MovieFragment::class.simpleName
    }

    private lateinit var attributes: Attribute
    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var daysListAdapter: DaysListAdapter

    //sharedViewModel - navGraph scope
    private val viewModelShared: SharedMovieViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
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
            movies = viewModelShared.movies
            retryCallback = object : RetryCallback {
                override fun retry() {
                    viewModelShared.retry()
                }
            }
        }


        viewModelShared.loadMovies()

        setupObservers()
        initMoviesRecyclerView()
        initDateTitleRecyclerView()
    }

    private fun setupObservers() {
        viewModelShared.movies.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                Log.d(TAG, it.toString())
                movieListAdapter.submitList(it.data)
            } else if (it.status == Status.ERROR) {
                movieListAdapter.submitList(listOf())
            }
        })

        viewModelShared.attributes.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                attributes = it.data!!
                daysListAdapter.submitList(it.data.days)
            }
        })

        viewModelShared.currentFilterAttribute.observe(viewLifecycleOwner, {
            binding.toolbar.subtitle = resources.getString(R.string.city_format_toolbar, it.city)
        })

    }

    private fun initMoviesRecyclerView() {
        val movieClickCallback = object : MovieViewClickCallback {
            override fun onClick(view: View, movies: Movies) {
                navigateToMovieDetailsFragment(view, movies)
            }
        }

        movieListAdapter = MovieListAdapter(movieClickCallback)

        binding.movieListGrid.adapter = movieListAdapter
    }

    private fun initDateTitleRecyclerView() {
        val dayTitleClickCallback = object : DayTitleViewClickCallback {
            override fun onClick(cardView: View, dateTitle: String) {
                viewModelShared.setDateMoviesTitle(dateTitle)
                viewModelShared.loadMovies()
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.filterFragmentMenu -> navigateToFilterFragment()
            R.id.watchlistFragmentMenu -> navigateToWatchListFragment()
        }
        return true
    }

    private fun navigateToMovieDetailsFragment(view: View, movies: Movies) {
        // Set exit and reenter transitions here as opposed to in onCreate because these transitions
        // will be set and overwritten on HomeFragment for other navigation actions.
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val movieDetailTransitionName =
                resources.getString(R.string.movie_detail_transition_name)

            val extras = FragmentNavigatorExtras(
                view to movieDetailTransitionName
            )

            val action = MovieFragmentDirections.showDetailsMovie(movies)

            findNavController().navigate(action, extras)
        } else {
            findNavController().navigate(MovieFragmentDirections.showDetailsMovie(movies))
        }
    }

    private fun navigateToFilterFragment() {
        // Set exit and reenter transitions here as opposed to in onCreate because these transitions
        // will be set and overwritten on HomeFragment for other navigation actions.
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
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
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        val directions = MovieFragmentDirections.showWatchlistFragment()
        findNavController().navigate(directions)
    }
}