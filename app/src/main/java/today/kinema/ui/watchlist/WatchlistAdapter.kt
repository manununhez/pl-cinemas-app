package today.kinema.ui.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.WatchlistItemBinding
import today.kinema.databinding.WatchlistItemHeaderBinding
import today.kinema.db.WatchlistMovie

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class WatchlistAdapter(
    private val watchlistITemClickCallback: WatchlistITemViewClickCallback
) : ListAdapter<WatchlistMovie, RecyclerView.ViewHolder>(WatchListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> WatchlistViewHolderHeader.from(parent)
            ITEM_VIEW_TYPE_ITEM -> WatchlistViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).header) {
            true -> ITEM_VIEW_TYPE_HEADER
            false -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WatchlistViewHolder -> {
                val favoriteMovie = getItem(position)
                holder.bind(favoriteMovie, watchlistITemClickCallback)
            }
            is WatchlistViewHolderHeader -> {
                val favoriteMovie = getItem(position)
                holder.bind(favoriteMovie, watchlistITemClickCallback)
            }
        }
    }

    class WatchlistViewHolder(val binding: WatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): WatchlistViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WatchlistItemBinding.inflate(layoutInflater, parent, false)
                return WatchlistViewHolder(binding)
            }
        }

        fun bind(item: WatchlistMovie, watchlistITemClickCallback: WatchlistITemViewClickCallback) {
            binding.apply {
                movies = item.movies
                ivClose.setOnClickListener {
                    watchlistITemClickCallback.removeFavoriteMovie(item)
                }
                watchlistItemCardView.setOnClickListener {
                    watchlistITemClickCallback.navigateTo(it, item)
                }

                executePendingBindings()
            }
        }
    }

    class WatchlistViewHolderHeader(val binding: WatchlistItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): WatchlistViewHolderHeader {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WatchlistItemHeaderBinding.inflate(layoutInflater, parent, false)
                return WatchlistViewHolderHeader(binding)
            }
        }

        fun bind(item: WatchlistMovie, watchlistITemClickCallback: WatchlistITemViewClickCallback) {
            binding.apply {
                dateHeader.text = item.dateTitle

                movies = item.movies
                ivClose.setOnClickListener {
                    watchlistITemClickCallback.removeFavoriteMovie(item)
                }
                watchlistItemCardView.setOnClickListener {
                    watchlistITemClickCallback.navigateTo(it, item)
                }

                executePendingBindings()
            }
        }
    }
}

class WatchListDiffCallback : DiffUtil.ItemCallback<WatchlistMovie>() {
    override fun areItemsTheSame(oldItem: WatchlistMovie, newItem: WatchlistMovie): Boolean =
        (oldItem.id == newItem.id && oldItem.dateTitle == newItem.dateTitle)

    override fun areContentsTheSame(oldItem: WatchlistMovie, newItem: WatchlistMovie): Boolean =
        oldItem.movies == newItem.movies
}


interface WatchlistITemViewClickCallback {
    fun removeFavoriteMovie(watchlistMovie: WatchlistMovie)
    fun navigateTo(view: View, watchlistMovie: WatchlistMovie)
}
