package com.manudev.cinemaspl.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.FragmentFilterBinding
import com.manudev.cinemaspl.ui.SharedMovieViewModel
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Locations
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterFragment : Fragment() {
    companion object {
        val TAG: String? = FilterFragment::class.simpleName
    }


    private lateinit var binding: FragmentFilterBinding
    private lateinit var locations: Locations
    private lateinit var selectedLocation: String

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


        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        locations = params.locations
        selectedLocation = params.selectedLocation
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val filterClickCallback = object :
            LocationViewClickCallback {
            override fun onClick(location: Location) {
                viewModelShared.setMoviesCity(location.city)
                findNavController().popBackStack()
            }
        }

        binding.rvFilterList.adapter =
            FilterLocationAdapter(
                locations.locations,
                viewModelShared.query,
                viewLifecycleOwner,
                filterClickCallback
            )
    }
}