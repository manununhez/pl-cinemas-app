package today.kinema.ui.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.R
import today.kinema.databinding.DayTitleItemBinding
import today.kinema.util.DateUtils
import today.kinema.vo.FilterAttribute
import java.util.*

class DaysListAdapter(
    private val dayTitleViewClickCallback: DayTitleViewClickCallback,
    private val currentAttributes: LiveData<FilterAttribute>,
    private val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<String, DaysListAdapter.DaysViewHolder>(DaysListDiffCallback()) {

    class DaysViewHolder(val binding: DayTitleItemBinding) : RecyclerView.ViewHolder(binding.root)

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
            val colorAttr = typedArray.getColor(0, 0)

            holder.binding.apply {
                tvWeekDay.setTextColor(colorAttr)
                tvDayTitle.setTextColor(colorAttr)
                tvMonthTitle.setTextColor(colorAttr)
                dayTitleCardView.strokeColor = colorAttr
                tvBackgroundOVerlay.visibility =
                    if (it.date == currentDayTitle) VISIBLE else GONE
            }

            typedArray.recycle()
        })

        holder.binding.apply {
            tvWeekDay.text = formatWeekDay(currentDayTitle)
            tvDayTitle.text = formatDateDay(currentDayTitle)
            tvMonthTitle.text = formatDateMonth(currentDayTitle)
            dayTitleCardView.setOnClickListener {
                dayTitleViewClickCallback.onClick(it, currentDayTitle)
            }

            executePendingBindings()
        }
    }

    private fun formatWeekDay(date: String): String {
        val currentDate = Date()
        val today = DateUtils.dateFormat(currentDate)
        val todayWeekName = DateUtils.weekDateFormat(currentDate)

        val formattedDay = DateUtils.dateFormat(date)
        val formattedWeekDay = DateUtils.weekDateFormat(date)
        return if (today == formattedDay && todayWeekName == formattedWeekDay)
            context.resources.getString(R.string.today)
        else formattedWeekDay
    }

    private fun formatDateDay(date: String) = DateUtils.dayDateFormat(date)

    private fun formatDateMonth(date: String) = DateUtils.monthDateFormat(date)
}

class DaysListDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}

interface DayTitleViewClickCallback {
    fun onClick(cardView: View, dateTitle: String)
}
