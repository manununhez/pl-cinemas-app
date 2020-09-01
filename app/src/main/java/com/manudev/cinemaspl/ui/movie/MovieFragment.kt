package com.manudev.cinemaspl.ui.movie

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
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.FragmentMovieBinding
import com.manudev.cinemaspl.ui.common.RetryCallback
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Locations
import com.manudev.cinemaspl.vo.Movies
import com.manudev.cinemaspl.vo.Status
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    companion object {
        private val TAG: String? = MovieFragment::class.simpleName
    }

    //internally using defaultViewModelProviderFactory
    private val viewModelShared: SharedMovieViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var locationsList: List<Location>

    private lateinit var binding: FragmentMovieBinding

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
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        viewModelShared.init()
        viewModelShared.loadMovies()
        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModelShared.loadMovies()
            }
        }
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun initRecyclerView() {
        val movieClickCallback = object : MovieViewClickCallback {
            override fun onClick(cardView: View, movies: Movies) {
                // Set exit and reenter transitions here as opposed to in onCreate because these transitions
                // will be set and overwritten on HomeFragment for other navigation actions.
                exitTransition = MaterialElevationScale(false).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }

                val movieDetailTransitionName = getString(R.string.movie_detail_transition_name)
                val extras = FragmentNavigatorExtras(
                    cardView to movieDetailTransitionName
                )
                val action = MovieFragmentDirections.showDetailsMovie(movies)

                findNavController().navigate(action, extras)
            }

        }


        val movieListAdapter = MovieListAdapter(movieClickCallback)

        binding.movieListGrid.adapter = movieListAdapter
        binding.movies = viewModelShared.movies

        viewModelShared.movies.observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, it.toString())
                movieListAdapter.submitList(it.data)
            }
        })

        //TODO receive only data with couroutines!
        viewModelShared.locations.observe(viewLifecycleOwner, {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        locationsList = it.data!!
                    }

                    Status.ERROR -> {
                        //TODO complete
                    }
                    Status.LOADING -> {
                        //TODO complete
                    }
                }
            }
        })
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.filterFragmentMenu -> {
                findNavController().navigate( MovieFragmentDirections.showFilterFragment(Locations(locationsList), viewModelShared.query.value!!))
            }
        }
        return true
    }
}