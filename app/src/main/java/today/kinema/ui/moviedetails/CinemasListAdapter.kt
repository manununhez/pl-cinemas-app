package today.kinema.ui.moviedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.CinemaItemBinding
import today.kinema.vo.Cinema

class CinemasListAdapter(
    private val cinemaClickCallback: CinemaViewClickCallback
) : ListAdapter<Cinema, CinemasListAdapter.CinemaViewHolder>(CinemaListDiffCallback()) {

    class CinemaViewHolder(val binding: CinemaItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CinemaItemBinding.inflate(inflater)
        return CinemaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            cinema = item
            cinemaItemCardView.setOnClickListener {
                cinemaClickCallback.onClick(item)
            }
            executePendingBindings()
        }
    }
}

class CinemaListDiffCallback : DiffUtil.ItemCallback<Cinema>() {
    override fun areItemsTheSame(oldItem: Cinema, newItem: Cinema): Boolean =
        (oldItem.cinemaId == newItem.cinemaId)

    override fun areContentsTheSame(oldItem: Cinema, newItem: Cinema): Boolean =
        oldItem == newItem
}

interface CinemaViewClickCallback {
    fun onClick(cinema: Cinema)
}
