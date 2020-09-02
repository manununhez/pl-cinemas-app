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

package com.manudev.cinemaspl.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.databinding.DayTitleItemBinding
import com.manudev.cinemaspl.vo.DayTitle
import java.text.SimpleDateFormat
import java.util.*

class DaysListAdapter(
    private val dayTitleViewClickCallback: DayTitleViewClickCallback
) : ListAdapter<DayTitle, DaysListAdapter.DaysViewHolder>(Companion) {

    class DaysViewHolder(val binding: DayTitleItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<DayTitle>() {
        override fun areItemsTheSame(oldItem: DayTitle, newItem: DayTitle): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: DayTitle, newItem: DayTitle): Boolean =
            oldItem.date == newItem.date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DayTitleItemBinding.inflate(layoutInflater, parent, false)

        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val currentDayTitle = getItem(position)

        holder.binding.tvDayTitle.text = formatDateDay(currentDayTitle.date)
        holder.binding.tvMonthTitle.text = formatDateMonth(currentDayTitle.date)
        holder.binding.dayTitleCardView.setOnClickListener {
            dayTitleViewClickCallback.onClick(it, currentDayTitle)
        }
        holder.binding.executePendingBindings()
    }

    private fun formatDateDay(date: String): String {
        val formatOutputDay = SimpleDateFormat("dd", Locale.getDefault())
        val timestamp = DayTitle.dateFormat().parse(date)?.time
        return formatOutputDay.format(timestamp)
    }

    private fun formatDateMonth(date: String): String {
        val formatOutputMonth = SimpleDateFormat("MMM", Locale.getDefault())
        val timestamp = DayTitle.dateFormat().parse(date)?.time
        val formatted = formatOutputMonth.format(timestamp)
        return formatted.toUpperCase(Locale.getDefault()).replace(".", "")
    }
}

interface DayTitleViewClickCallback {
    fun onClick(cardView: View, dayTitle: DayTitle)
}
