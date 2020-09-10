package com.manudev.cinemaspl.ui.filter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.LocationItemBinding
import com.manudev.cinemaspl.ui.SharedMovieViewModel
import com.manudev.cinemaspl.vo.Location

class FilterLocationAdapter(
    private val items: List<Location>,
    private val selectedLocation: LiveData<SharedMovieViewModel.CinemaMoviesId>,
    private val viewLifecycleOwner: LifecycleOwner,
    private val locationViewClickCallback: LocationViewClickCallback
) :
    RecyclerView.Adapter<FilterLocationAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LocationItemBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            context,
            items[position],
            selectedLocation,
            viewLifecycleOwner,
            locationViewClickCallback
        )

    class ViewHolder(val binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            item: Location,
            selectedLocation: LiveData<SharedMovieViewModel.CinemaMoviesId>,
            viewLifecycleOwner: LifecycleOwner,
            locationViewClickCallback: LocationViewClickCallback
        ) {
            //TODO trying not to use LiveData here!
            selectedLocation.observe(viewLifecycleOwner, {
                if (item.city == it.city) {
                    binding.textView.setTextColor(context.resources.getColor(R.color.cinepl_deep_orange_700))
                    binding.locationCardItem.strokeColor =
                        context.resources.getColor(R.color.cinepl_deep_orange_700)
                    binding.locationCardItem.strokeWidth = 1
                } else {
                    val typedArray =
                        context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))

                    binding.textView.setTextColor(typedArray.getColor(0, 0))
                    binding.locationCardItem.strokeColor = Color.TRANSPARENT
                    binding.locationCardItem.strokeWidth = 0

                    typedArray.recycle()

                }
            })

            binding.location = item
            binding.textView.setOnClickListener {
                locationViewClickCallback.onClick(item)
            }
            binding.executePendingBindings()
        }
    }
}

interface LocationViewClickCallback {
    fun onClick(location: Location)
}
