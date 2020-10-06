package today.kinema.ui.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import today.kinema.R
import today.kinema.databinding.FilterItemMultipleChoiceBinding
import today.kinema.vo.FilterAttribute

class FilterCinemaAdapter(
    private val items: List<String>,
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val cinemaViewClickCallback: FilterCinemaViewClickCallback
) :
    RecyclerView.Adapter<FilterCinemaAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemMultipleChoiceBinding.inflate(
            inflater,
            parent,
            false
        ) //use this to use LinearLayoutManager instead of StaggeredGridLayoutManager
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = (items.size + 1) //+1 to add the Select all header

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemValue =
            if (position == 0) context.resources.getString(R.string.select_all) else items[position - 1]

        holder.bind(
            context,
            itemValue,
            currentAttribute,
            lifecycleOwner,
            cinemaViewClickCallback
        )
    }

    class ViewHolder(val binding: FilterItemMultipleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            item: String,
            currentAttribute: LiveData<FilterAttribute>,
            viewLifecycleOwner: LifecycleOwner,
            cinemaViewClickCallback: FilterCinemaViewClickCallback
        ) {
            //TODO trying not to use LiveData here!
            currentAttribute.observe(viewLifecycleOwner, {
                binding.apply {
                    if (item == context.resources.getString(R.string.select_all) && it.cinema.isEmpty()) { //Select all is checked!, so other options are unchecked
                        checkBox.isChecked = true

                        tvBackgroundOVerlay.visibility = VISIBLE
                    } else {
                        checkBox.isChecked = it.cinema.contains(item)

                        tvBackgroundOVerlay.visibility =
                            if (checkBox.isChecked) VISIBLE else GONE
                    }
                }
            })

            binding.apply {
                location = item
                checkBox.setOnClickListener {
                    cinemaViewClickCallback.onClick(
                        item,
                        item == context.resources.getString(R.string.select_all)
                    )
                }
                executePendingBindings()
            }

        }
    }
}

interface FilterCinemaViewClickCallback {
    fun onClick(cinema: String, clearSelection: Boolean)
}
