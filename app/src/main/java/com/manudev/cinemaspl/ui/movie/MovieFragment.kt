package com.manudev.cinemaspl.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.transition.MaterialElevationScale
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.FragmentMovieBinding
import com.manudev.cinemaspl.ui.common.RetryCallback
import com.manudev.cinemaspl.vo.Movies
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieFragment : Fragment() {

    //internally using defaultViewModelProviderFactory
    private val viewModel: MovieViewModel by viewModels()

    private lateinit var binding: FragmentMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentMovieBinding>(
            inflater,
            R.layout.fragment_movie,
            container,
            false
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.loadMovies()
            }
        }
        binding = dataBinding
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        viewModel.init()
        viewModel.loadMovies()

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
        binding.movies = viewModel.movies

        viewModel.movies.observe(viewLifecycleOwner, {
            it?.let {
                Log.e("MainActivity", it.toString())
                movieListAdapter.submitList(it.data)
            }
        })
    }
}