package today.kinema.ui.watchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.WatchlistItemBinding
import today.kinema.vo.Movies

class WatchlistAdapter(
    private val watchlistITemClickCallback: WatchlistITemViewClickCallback
) : ListAdapter<Movies, WatchlistAdapter.WatchlistViewHolder>(WatchlistAdapter) {

    class WatchlistViewHolder(val binding: WatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean =
            oldItem.movie.id == newItem.movie.id
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WatchlistItemBinding.inflate(layoutInflater, parent, false)
        context = parent.context

        return WatchlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            movies = item
            ivClose.setOnClickListener {
                watchlistITemClickCallback.setWatchlist(item)
            }
            watchlistItemCardView.setOnClickListener {
                watchlistITemClickCallback.navigateTo(it, item)
            }

            executePendingBindings()
        }
    }
}

interface WatchlistITemViewClickCallback {
    fun setWatchlist(movie: Movies)
    fun navigateTo(view: View, movie: Movies)
}
