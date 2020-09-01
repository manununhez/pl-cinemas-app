/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manudev.cinemaspl.ui.filter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.databinding.LocationItemBinding
import com.manudev.cinemaspl.vo.Location

class FilterLocationAdapter(
    private val items: List<Location>,
    private val selectedLocation: LiveData<String>,
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
            selectedLocation: LiveData<String>,
            viewLifecycleOwner: LifecycleOwner,
            locationViewClickCallback: LocationViewClickCallback
        ) {
            //TODO trying not to use LiveData here!
            selectedLocation.observe(viewLifecycleOwner, {
                if (it.isEmpty() && item.city == "Warszawa") {
                    binding.textView.setTextColor(Color.parseColor("#d84315"))
                    binding.locationCardItem.strokeColor = Color.parseColor("#d84315")
                    binding.locationCardItem.strokeWidth = 1
                } else if (item.city == it) {
                    binding.textView.setTextColor(Color.parseColor("#d84315"))
                    binding.locationCardItem.strokeColor = Color.parseColor("#d84315")
                    binding.locationCardItem.strokeWidth = 1
                } else {
                    binding.textView.setTextColor(context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary)).getColor(0, 0))
                    binding.locationCardItem.strokeColor = Color.TRANSPARENT
                    binding.locationCardItem.strokeWidth = 0

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
