package com.manudev.cinemaspl.ui.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.databinding.FilterItemSingleChoiceBinding
import com.manudev.cinemaspl.vo.FilterAttribute


class FilterLocationAdapter(
    private val items: List<String>,
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val locationViewClickCallback: LocationViewClickCallback
) :
    RecyclerView.Adapter<FilterLocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemSingleChoiceBinding.inflate(inflater, parent, false) //use this to use LinearLayoutManager instead of StaggeredGridLayoutManager
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            items[position],
            currentAttribute,
            lifecycleOwner,
            locationViewClickCallback
        )

    class ViewHolder(val binding: FilterItemSingleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: String,
            currentAttribute: LiveData<FilterAttribute>,
            viewLifecycleOwner: LifecycleOwner,
            locationViewClickCallback: LocationViewClickCallback
        ) {

            //TODO trying not to use LiveData here!
            currentAttribute.observe(viewLifecycleOwner, {
                binding.radio.isChecked = item == it.city

                binding.tvBackgroundOVerlay.visibility = if(binding.radio.isChecked) View.VISIBLE else View.GONE
            })

            binding.location = item
            binding.radio.setOnClickListener {
                locationViewClickCallback.onClick(item)
            }
            binding.executePendingBindings()
        }
    }
}

interface LocationViewClickCallback {
    fun onClick(location: String)
}
