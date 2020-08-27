package com.manudev.cinemaspl.ui.moviedetails

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.FragmentDetailsMovieBinding
import com.manudev.cinemaspl.vo.Cinema
import com.manudev.cinemaspl.vo.Movies
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details_movie.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsMovieBinding
    private lateinit var movies: Movies

    private val params by navArgs<MovieDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentDetailsMovieBinding>(
            inflater,
            R.layout.fragment_details_movie,
            container,
            false
        )
        binding = dataBinding

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

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies = params.movies;
        binding.movies = movies
        binding.duration.text = if(movies.movie.duration == "0") "" else getString(R.string.movie_duration, movies.movie.duration)

        initRecyclerView()

        binding.detailsMovieCallback = object : DetailsMovieCallback {
            override fun watchTrailer() {
                requireActivity().startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(movies.movie.trailerUrl)
                    )
                )
            }

            override fun expandCollapseDescription() {
                if (expandCollapseOption.text == getString(R.string.description_text_collapsed)) {
//                    TransitionManager.beginDelayedTransition(
//                        menu_item_constraint_layout,
//                        AutoTransition()
//                    )
                    expandCollapseOption.text = getString(R.string.description_text_expanded)
                    movieDescription.maxLines = Integer.MAX_VALUE
                    movieDescription.ellipsize = null
                } else {
//                    TransitionManager.beginDelayedTransition(
//                        menu_item_constraint_layout,
//                        AutoTransition()
//                    )
                    expandCollapseOption.text = getString(R.string.description_text_collapsed)
                    movieDescription.maxLines = resources.getInteger(R.integer.max_lines_collapsed)
                    movieDescription.ellipsize = TextUtils.TruncateAt.END
                }

            }
        }

        movieDescription.post {
            if (movieDescription.lineCount <= resources.getInteger(R.integer.max_lines_collapsed))
                expandCollapseOption.visibility = View.GONE
        }

    }

    private fun initRecyclerView() {
        val cinemaClickCallback = object :
            CinemaViewClickCallback {
            override fun onClick(cinema: Cinema) {
                requireActivity().startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(cinema.cinemaPageUrl)
                    )
                )
            }

        }

        val cinemaListAdapter = CinemaMovieListAdapter(movies.cinemas, cinemaClickCallback)
        binding.rvCinemaList.adapter = cinemaListAdapter

    }
}