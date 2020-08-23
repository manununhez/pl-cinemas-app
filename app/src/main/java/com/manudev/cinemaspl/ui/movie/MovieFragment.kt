package com.manudev.cinemaspl.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.vo.Status
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MovieFragment : Fragment() {

    //internally using defaultViewModelProviderFactory
    private val viewModel: MovieViewModel by viewModels()

    private val TAG = MovieFragment::getTag.name
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            //findNavController().navigate(R.id.action_MovieFragment_to_MovieDetailsFragment)
            viewModel.refreshMovies()
        }

        viewModel.init()
        viewModel.refreshMovies()
        viewModel.movies.observe(viewLifecycleOwner, {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        Log.d(TAG, "SUCCESS")
                    }
                    Status.ERROR ->
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    Status.LOADING ->
                        Log.d(TAG, "LOADING")
                }
            }
        })
    }
}