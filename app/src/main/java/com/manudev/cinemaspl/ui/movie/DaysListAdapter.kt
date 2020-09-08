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

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.R
import com.manudev.cinemaspl.databinding.DayTitleItemBinding
import com.manudev.cinemaspl.ui.SharedMovieViewModel
import com.manudev.cinemaspl.vo.DayTitle
import java.text.SimpleDateFormat
import java.util.*

class DaysListAdapter(
    private val dayTitleViewClickCallback: DayTitleViewClickCallback,
    private val selectedDate: LiveData<SharedMovieViewModel.CinemaMoviesId>,
    private val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<DayTitle, DaysListAdapter.DaysViewHolder>(Companion) {

    class DaysViewHolder(val binding: DayTitleItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<DayTitle>() {
        override fun areItemsTheSame(oldItem: DayTitle, newItem: DayTitle): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: DayTitle, newItem: DayTitle): Boolean =
            oldItem.date == newItem.date
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DayTitleItemBinding.inflate(layoutInflater, parent, false)
        context = parent.context

        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val currentDayTitle = getItem(position)

        selectedDate.observe(viewLifecycleOwner, {
            if (it.date == currentDayTitle.date) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    holder.binding.tvDayTitle.setTextAppearance(R.style.TextAppearance_Cinema_Headline4)
//                }
                holder.binding.tvWeekDay.setTextColor(context.resources.getColor(R.color.cinepl_deep_orange_700))
                holder.binding.tvDayTitle.setTextColor(context.resources.getColor(R.color.cinepl_deep_orange_700))
                holder.binding.tvMonthTitle.setTextColor(context.resources.getColor(R.color.cinepl_deep_orange_700))
                holder.binding.dayTitleCardView.strokeColor = context.resources.getColor(R.color.cinepl_deep_orange_700)
                holder.binding.dayTitleCardView.strokeWidth = 1
            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    holder.binding.tvDayTitle.setTextAppearance(R.style.TextAppearance_Cinema_Headline5)
//                }
                holder.binding.tvWeekDay.setTextColor(context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
                    .getColor(0, 0))
                holder.binding.tvDayTitle.setTextColor(context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
                    .getColor(0, 0))
                holder.binding.tvMonthTitle.setTextColor(context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
                    .getColor(0, 0))
                holder.binding.dayTitleCardView.strokeColor = Color.GRAY
                holder.binding.dayTitleCardView.strokeWidth = 1
            }
        })

        holder.binding.tvWeekDay.text = formatWeekDay(currentDayTitle.date)
        holder.binding.tvDayTitle.text = formatDateDay(currentDayTitle.date)
        holder.binding.tvMonthTitle.text = formatDateMonth(currentDayTitle.date)
        holder.binding.dayTitleCardView.setOnClickListener {
            dayTitleViewClickCallback.onClick(it, currentDayTitle)
        }

        holder.binding.executePendingBindings()
    }

    private fun formatWeekDay(date: String): String {
        val formatOutputDay = SimpleDateFormat("EEE", Locale.getDefault())
        val timestamp = DayTitle.dateFormat().parse(date)?.time

        val today = DayTitle.currentDateWeekName()
        val formattedWeekDay = formatOutputDay.format(timestamp)
        return if (today == formattedWeekDay)
            "Today"
        else formattedWeekDay
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
