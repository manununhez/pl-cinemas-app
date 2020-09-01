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
import com.manudev.cinemaspl.ui.movie.SharedMovieViewModel
import com.manudev.cinemaspl.vo.Location
import com.manudev.cinemaspl.vo.Locations
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FilterFragment : Fragment() {
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


        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        locations = params.locations;
        selectedLocation = params.selectedLocation;
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val filterClickCallback = object :
            LocationViewClickCallback {
            override fun onClick(location: Location) {
                viewModelShared.setMoviesCity(location.city)
            }
        }

        binding.rvFilterList.adapter =
            FilterLocationAdapter(locations.locations, viewModelShared.query, viewLifecycleOwner, filterClickCallback)
    }
}