package com.manudev.cinemaspl.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.FragmentFilterBinding
import com.manudev.cinemaspl.ui.SharedMovieViewModel
import com.manudev.cinemaspl.vo.Attribute
import com.manudev.cinemaspl.vo.FilterAttribute
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var attributes: Attribute
    private lateinit var filteredAttributes: FilterAttribute

    //SharedViewModel
    private val viewModelShared: SharedMovieViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }
    private val params by navArgs<FilterFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFilterBinding.inflate(inflater, container, false).apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }


//        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
//            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//        }
//
//        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
//            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        attributes = params.attributes
        filteredAttributes = params.filteredAttributes

        initFilterCitiesRecyclerView()
        initFilterCinemasRecyclerView()
        initFilterLanguagesRecyclerView()
    }

    private fun initFilterCitiesRecyclerView() {
        val filterClickCallback = object :
            LocationViewClickCallback {
            override fun onClick(location: String) {
                viewModelShared.setMoviesCity(location)
            }
        }

        binding.rvFilterCitiesList.setHasFixedSize(true)

        binding.rvFilterCitiesList.adapter =
            FilterLocationAdapter(
                attributes.cities,
                viewModelShared.currentFilterAttribute,
                viewLifecycleOwner,
                filterClickCallback
            )


    }

    private fun initFilterCinemasRecyclerView() {
        val filterCinemaClickCallback = object :
            FilterCinemaViewClickCallback {
            override fun onClick(cinema: String) {
                viewModelShared.setMoviesCinemas(cinema)
            }
        }

        binding.rvFilterCinemasList.setHasFixedSize(true)

        binding.rvFilterCinemasList.adapter =
            FilterCinemaAdapter(
                attributes.cinemas,
                viewModelShared.currentFilterAttribute,
                viewLifecycleOwner,
                filterCinemaClickCallback
            )

    }

    private fun initFilterLanguagesRecyclerView() {
        val filterLanguageClickCallback = object :
            FilterLanguageViewClickCallback {
            override fun onClick(language: String) {
                viewModelShared.setMoviesLanguage(language)
            }
        }

        binding.rvFilterLanguagesList.setHasFixedSize(true)

        binding.rvFilterLanguagesList.adapter =
            FilterLanguageAdapter(
                attributes.languages,
                viewModelShared.currentFilterAttribute,
                viewLifecycleOwner,
                filterLanguageClickCallback
            )
    }
}