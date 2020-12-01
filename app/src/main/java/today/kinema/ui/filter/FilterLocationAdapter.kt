package today.kinema.ui.filter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.FilterItemSingleChoiceBinding
import today.kinema.vo.FilterAttribute


class FilterLocationAdapter(
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val locationViewClickCallback: LocationViewClickCallback
) : ListAdapter<String, FilterLocationAdapter.FilterLocationViewHolder>(
    FilterLocationListDiffCallback()
) {

    class FilterLocationViewHolder(val binding: FilterItemSingleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterLocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemSingleChoiceBinding.inflate(
            inflater,
            parent,
            false
        ) //use this to use LinearLayoutManager instead of StaggeredGridLayoutManager
        return FilterLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterLocationViewHolder, position: Int) {
        val currentItem = getItem(position)
        val binding = holder.binding

        currentAttribute.observe(lifecycleOwner, {
            binding.apply {
                radio.isChecked = (currentItem == it.city)
                tvBackgroundOVerlay.visibility = if (radio.isChecked) VISIBLE else GONE
            }
        })

        binding.apply {
            item = currentItem
            radio.setOnClickListener {
                locationViewClickCallback.onClick(currentItem)
            }
            executePendingBindings()
        }
    }
}

class FilterLocationListDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}

interface LocationViewClickCallback {
    fun onClick(location: String)
}
