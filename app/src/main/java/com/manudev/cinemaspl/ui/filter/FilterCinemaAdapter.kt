package com.manudev.cinemaspl.ui.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.databinding.FilterItemMultipleChoiceBinding
import com.manudev.cinemaspl.vo.FilterAttribute

class FilterCinemaAdapter(
    private val items: List<String>,
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val cinemaViewClickCallback: FilterCinemaViewClickCallback
) :
    RecyclerView.Adapter<FilterCinemaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemMultipleChoiceBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            items[position],
            currentAttribute,
            lifecycleOwner,
            cinemaViewClickCallback
        )

    class ViewHolder(val binding: FilterItemMultipleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: String,
            currentAttribute: LiveData<FilterAttribute>,
            viewLifecycleOwner: LifecycleOwner,
            cinemaViewClickCallback: FilterCinemaViewClickCallback
        ) {
            currentAttribute.observe(viewLifecycleOwner, {
                binding.checkBox.isChecked = it.cinema.contains(item)
            })

            binding.location = item
            binding.checkBox.setOnClickListener {
                cinemaViewClickCallback.onClick(item)
            }
            binding.executePendingBindings()
        }
    }
}

interface FilterCinemaViewClickCallback {
    fun onClick(cinema: String)
}
