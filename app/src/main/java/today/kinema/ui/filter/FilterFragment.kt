package today.kinema.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import today.kinema.R
import today.kinema.databinding.FragmentFilterBinding
import kotlin.LazyThreadSafetyMode.NONE


@AndroidEntryPoint
class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding

    private val params by navArgs<FilterFragmentArgs>()
    private val attributes by lazy(NONE) {
        params.attributes
    }

    //SharedViewModel
    private val viewModelShared: FilterViewModel by viewModels()

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

        binding = FragmentFilterBinding.inflate(inflater, container, false).apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.title = resources.getString(R.string.menu_item_filter)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

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

        binding.rvFilterCitiesList.apply {
            setHasFixedSize(true)
            adapter =
                FilterLocationAdapter(
                    attributes.cities,
                    viewModelShared.currentFilterAttribute,
                    viewLifecycleOwner,
                    filterClickCallback
                )
        }

        //scroll rv to selected city
        viewModelShared.currentFilterAttribute.observe(viewLifecycleOwner, {
            binding.rvFilterCitiesList.scrollToPosition(attributes.cities.indexOf(it.city)) //scroll to selected city
            if (it.language.isNotEmpty())
                binding.rvFilterLanguagesList.scrollToPosition(attributes.languages.indexOf(it.language[0])) //scroll to the first select element
            if (it.cinema.isNotEmpty())
                binding.rvFilterCinemasList.scrollToPosition(attributes.cinemas.indexOf(it.cinema[0])) //scroll to the first select element
        })
    }

    private fun initFilterCinemasRecyclerView() {
        val filterCinemaClickCallback = object :
            FilterCinemaViewClickCallback {
            override fun onClick(cinema: String, clearSelection: Boolean) {
                viewModelShared.setMoviesCinemas(cinema, clearSelection)
            }
        }

        binding.rvFilterCinemasList.apply {
            setHasFixedSize(true)
            adapter =
                FilterCinemaAdapter(
                    attributes.cinemas,
                    viewModelShared.currentFilterAttribute,
                    viewLifecycleOwner,
                    filterCinemaClickCallback
                )
        }

    }

    private fun initFilterLanguagesRecyclerView() {
        val filterLanguageClickCallback = object :
            FilterLanguageViewClickCallback {
            override fun onClick(language: String, clearSelection: Boolean) {
                viewModelShared.setMoviesLanguage(language, clearSelection)
            }
        }

        binding.rvFilterLanguagesList.apply {
            setHasFixedSize(true)

            adapter =
                FilterLanguageAdapter(
                    attributes.languages,
                    viewModelShared.currentFilterAttribute,
                    viewLifecycleOwner,
                    filterLanguageClickCallback
                )
        }

    }
}