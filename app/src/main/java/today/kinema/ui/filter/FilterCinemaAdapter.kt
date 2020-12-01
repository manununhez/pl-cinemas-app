package today.kinema.ui.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.R
import today.kinema.databinding.FilterItemMultipleChoiceBinding
import today.kinema.vo.FilterAttribute

class FilterCinemaAdapter(
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val cinemaViewClickCallback: FilterCinemaViewClickCallback
) : ListAdapter<String, FilterCinemaAdapter.FilterCinemaViewHolder>(
    FilterCinemaListDiffCallback()
) {

    class FilterCinemaViewHolder(val binding: FilterItemMultipleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCinemaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemMultipleChoiceBinding.inflate(
            inflater,
            parent,
            false
        ) //use this to use LinearLayoutManager instead of StaggeredGridLayoutManager
        context = parent.context
        return FilterCinemaViewHolder(binding)
    }

    //+1 to add the Select all header
    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun onBindViewHolder(holder: FilterCinemaViewHolder, position: Int) {
        val binding = holder.binding
        val itemValue =
            if (position == 0) context.resources.getString(R.string.select_all) else getItem(position - 1)

        currentAttribute.observe(lifecycleOwner, {
            binding.apply {
                if (itemValue == context.resources.getString(R.string.select_all) && it.cinema.isEmpty()) { //Select all is checked!, so other options are unchecked
                    checkBox.isChecked = true

                    tvBackgroundOVerlay.visibility = VISIBLE
                } else {
                    checkBox.isChecked = it.cinema.contains(itemValue)

                    tvBackgroundOVerlay.visibility =
                        if (checkBox.isChecked) VISIBLE else GONE
                }
            }
        })

        binding.apply {
            item = itemValue
            checkBox.setOnClickListener {
                cinemaViewClickCallback.onClick(
                    itemValue,
                    itemValue == context.resources.getString(R.string.select_all)
                )
            }
            executePendingBindings()
        }
    }
}

class FilterCinemaListDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}

interface FilterCinemaViewClickCallback {
    fun onClick(cinema: String, clearSelection: Boolean)
}
