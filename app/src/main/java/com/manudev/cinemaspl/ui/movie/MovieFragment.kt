package com.manudev.cinemaspl.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.FragmentMovieBinding
import com.manudev.cinemaspl.ui.common.RetryCallback
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieFragment : Fragment() {

    //internally using defaultViewModelProviderFactory
    private val viewModel: MovieViewModel by viewModels()

    private val TAG = MovieFragment::getTag.name

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
//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            //findNavController().navigate(R.id.action_MovieFragment_to_MovieDetailsFragment)
//            viewModel.refreshMovies()
//        }


        viewModel.init()
        viewModel.loadMovies()


    }

    private fun initRecyclerView() {
        val movieListAdapter =  MovieListAdapter { movies ->
            findNavController().navigate(
                MovieFragmentDirections.showDetailsMovie(movies)
            )
        }
        binding.movieListGrid.adapter = movieListAdapter
        binding.movies = viewModel.movies

        viewModel.movies.observe(viewLifecycleOwner, {
            it?.let {
                Log.e("MainActivity", it.toString())
                movieListAdapter.submitList(it.data)
        }})
    }
}