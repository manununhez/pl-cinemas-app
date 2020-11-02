package today.kinema.ui.filter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.FilterItemSingleChoiceBinding
import today.kinema.vo.FilterAttribute


class FilterLocationAdapter(
    private val items: List<String>,
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val locationViewClickCallback: LocationViewClickCallback
) :
    RecyclerView.Adapter<FilterLocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemSingleChoiceBinding.inflate(
            inflater,
            parent,
            false
        ) //use this to use LinearLayoutManager instead of StaggeredGridLayoutManager
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
                binding.apply {
                    radio.isChecked = (item == it.city)

                    tvBackgroundOVerlay.visibility = if (radio.isChecked) VISIBLE else GONE
                }
            })

            binding.apply {
                this.item = item
                radio.setOnClickListener {
                    locationViewClickCallback.onClick(item)
                }
                executePendingBindings()
            }
        }
    }
}

interface LocationViewClickCallback {
    fun onClick(location: String)
}
