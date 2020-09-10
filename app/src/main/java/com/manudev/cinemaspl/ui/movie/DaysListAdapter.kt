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
import com.manudev.cinemaspl.vo.DateTitle
import java.util.*

class DaysListAdapter(
    private val dayTitleViewClickCallback: DayTitleViewClickCallback,
    private val selectedDate: LiveData<SharedMovieViewModel.CinemaMoviesId>,
    private val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<DateTitle, DaysListAdapter.DaysViewHolder>(Companion) {

    class DaysViewHolder(val binding: DayTitleItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<DateTitle>() {
        override fun areItemsTheSame(oldItem: DateTitle, newItem: DateTitle): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: DateTitle, newItem: DateTitle): Boolean =
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
                holder.binding.dayTitleCardView.strokeColor =
                    context.resources.getColor(R.color.cinepl_deep_orange_700)
                holder.binding.dayTitleCardView.strokeWidth = 1
            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    holder.binding.tvDayTitle.setTextAppearance(R.style.TextAppearance_Cinema_Headline5)
//                }

                val typedArray =
                    context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))

                holder.binding.tvWeekDay.setTextColor(typedArray.getColor(0, 0))
                holder.binding.tvDayTitle.setTextColor(typedArray.getColor(0, 0))
                holder.binding.tvMonthTitle.setTextColor(typedArray.getColor(0, 0))
                holder.binding.dayTitleCardView.strokeColor = Color.GRAY
                holder.binding.dayTitleCardView.strokeWidth = 1

                typedArray.recycle()
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
        val currentDate = Date()
        val today = DateTitle.dateFormat(currentDate)
        val todayWeekName = DateTitle.weekDateFormat(currentDate)

        val formattedDay = DateTitle.dateFormat(date)
        val formattedWeekDay = DateTitle.weekDateFormat(date)
        return if (today == formattedDay && todayWeekName == formattedWeekDay)
            context.getString(R.string.today)
        else formattedWeekDay
    }

    private fun formatDateDay(date: String) = DateTitle.dayDateFormat(date)

    private fun formatDateMonth(date: String) = DateTitle.monthDateFormat(date)
}

interface DayTitleViewClickCallback {
    fun onClick(cardView: View, dateTitle: DateTitle)
}
