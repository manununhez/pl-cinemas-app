package today.kinema.ui.watchlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import today.kinema.R
import today.kinema.databinding.FragmentWatchlistBinding
import today.kinema.ui.SharedMovieViewModel
import today.kinema.vo.Movies

@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var watchlistAdapter: WatchlistAdapter

    //SharedViewModel
    private val viewModelShared: SharedMovieViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWatchlistBinding.inflate(inflater, container, false).apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.title = resources.getString(R.string.menu_item_watchlist)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Postpone enter transitions to allow shared element transitions to run.
        // https://github.com/googlesamples/android-architecture-components/issues/495
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.movies = viewModelShared.watchlist

        initWatchlistRecyclerView()

        setupObservers()
    }

    private fun setupObservers() {
        //Update watchlist
        viewModelShared.watchlist.observe(viewLifecycleOwner, {
            watchlistAdapter.submitList(it)
        })
    }

    private fun initWatchlistRecyclerView() {
        val watchlistITemClickCallback = object :
            WatchlistITemViewClickCallback {
            override fun setWatchlist(movie: Movies) {
                viewModelShared.setWatchlist(movie)
            }

            override fun navigateTo(view: View, movie: Movies) {
                navigateToMovieDetailsFragment(view, movie)
            }
        }

        binding.rvWatchlist.apply {
            setHasFixedSize(true)
            watchlistAdapter = WatchlistAdapter(watchlistITemClickCallback)
            adapter = watchlistAdapter

        }
    }

    private fun navigateToMovieDetailsFragment(view: View, movie: Movies) {
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
            val action = WatchlistFragmentDirections.showDetailsMovie(movie)

            findNavController().navigate(action, extras)
        } else {
            findNavController().navigate(WatchlistFragmentDirections.showDetailsMovie(movie))
        }
    }
}