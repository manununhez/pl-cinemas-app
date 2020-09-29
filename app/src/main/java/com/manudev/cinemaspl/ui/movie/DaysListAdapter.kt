package com.manudev.cinemaspl.ui.movie

import android.content.Context
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
import com.manudev.cinemaspl.util.DateUtils
import com.manudev.cinemaspl.vo.FilterAttribute
import java.util.*

class DaysListAdapter(
    private val dayTitleViewClickCallback: DayTitleViewClickCallback,
    private val currentAttributes: LiveData<FilterAttribute>,
    private val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<String, DaysListAdapter.DaysViewHolder>(Companion) {

    class DaysViewHolder(val binding: DayTitleItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
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

        //TODO trying not to use LiveData here!
        currentAttributes.observe(viewLifecycleOwner, {
            val typedArray = if (it.date == currentDayTitle)
                context.obtainStyledAttributes(intArrayOf(R.attr.colorSecondary)) else context.obtainStyledAttributes(
                intArrayOf(R.attr.colorOnSurface)
            )

            holder.binding.tvWeekDay.setTextColor(typedArray.getColor(0, 0))
            holder.binding.tvDayTitle.setTextColor(typedArray.getColor(0, 0))
            holder.binding.tvMonthTitle.setTextColor(typedArray.getColor(0, 0))
            holder.binding.dayTitleCardView.strokeColor = typedArray.getColor(0, 0)
            holder.binding.tvBackgroundOVerlay.visibility =
                if (it.date == currentDayTitle) View.VISIBLE else View.GONE

            typedArray.recycle()
        })

        holder.binding.tvWeekDay.text = formatWeekDay(currentDayTitle)
        holder.binding.tvDayTitle.text = formatDateDay(currentDayTitle)
        holder.binding.tvMonthTitle.text = formatDateMonth(currentDayTitle)
        holder.binding.dayTitleCardView.setOnClickListener {
            dayTitleViewClickCallback.onClick(it, currentDayTitle)
        }

        holder.binding.executePendingBindings()
    }

    private fun formatWeekDay(date: String): String {
        val currentDate = Date()
        val today = DateUtils.dateFormat(currentDate)
        val todayWeekName = DateUtils.weekDateFormat(currentDate)

        val formattedDay = DateUtils.dateFormat(date)
        val formattedWeekDay = DateUtils.weekDateFormat(date)
        return if (today == formattedDay && todayWeekName == formattedWeekDay)
            context.getString(R.string.today)
        else formattedWeekDay
    }

    private fun formatDateDay(date: String) = DateUtils.dayDateFormat(date)

    private fun formatDateMonth(date: String) = DateUtils.monthDateFormat(date)
}

interface DayTitleViewClickCallback {
    fun onClick(cardView: View, dateTitle: String)
}
