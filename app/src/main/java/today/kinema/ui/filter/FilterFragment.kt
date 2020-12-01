package today.kinema.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import today.kinema.R
import today.kinema.databinding.FragmentFilterBinding
import today.kinema.vo.Attribute


@AndroidEntryPoint
class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var attributes: Attribute
    private lateinit var filterLocationAdapter: FilterLocationAdapter
    private lateinit var filterLanguageAdapter: FilterLanguageAdapter
    private lateinit var filterCinemaAdapter: FilterCinemaAdapter

    //SharedViewModel
    private val viewModelShared: FilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }

        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
            duration = resources.getInteger(R.integer.kinema_motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFilterBinding.inflate(inflater, container, false)
//            .apply {
//            toolbar.setNavigationOnClickListener {
//                findNavController().navigateUp()
//            }
//            toolbar.title = resources.getString(R.string.filter_title)
//
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

        setupObserver()

        initFilterCinemasRecyclerView()
        initFilterCitiesRecyclerView()
        initFilterLanguagesRecyclerView()

    }

    private fun setupObserver() {
        viewModelShared.attributes.observe(viewLifecycleOwner, {
            Timber.d("Update adapters!")
            attributes = it
            filterLocationAdapter.submitList(it.cities)
            filterCinemaAdapter.submitList(it.cinemas)
            filterLanguageAdapter.submitList(it.languages)
        })
    }

    private fun initFilterCitiesRecyclerView() {
        val filterClickCallback = object :
            LocationViewClickCallback {
            override fun onClick(location: String) {
                viewModelShared.setMoviesCity(location)
            }
        }

        filterLocationAdapter = FilterLocationAdapter(
            viewModelShared.currentFilterAttribute,
            viewLifecycleOwner,
            filterClickCallback
        )

        binding.rvFilterCitiesList.apply {
            setHasFixedSize(true)
            adapter = filterLocationAdapter
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

        filterCinemaAdapter = FilterCinemaAdapter(
            viewModelShared.currentFilterAttribute,
            viewLifecycleOwner,
            filterCinemaClickCallback
        )

        binding.rvFilterCinemasList.apply {
            setHasFixedSize(true)
            adapter = filterCinemaAdapter
        }

    }

    private fun initFilterLanguagesRecyclerView() {
        val filterLanguageClickCallback = object :
            FilterLanguageViewClickCallback {
            override fun onClick(language: String, clearSelection: Boolean) {
                viewModelShared.setMoviesLanguage(language, clearSelection)
            }
        }

        filterLanguageAdapter = FilterLanguageAdapter(
            viewModelShared.currentFilterAttribute,
            viewLifecycleOwner,
            filterLanguageClickCallback
        )

        binding.rvFilterLanguagesList.apply {
            setHasFixedSize(true)
            adapter = filterLanguageAdapter
        }

    }
}