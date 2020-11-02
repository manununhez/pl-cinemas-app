package today.kinema.ui.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import today.kinema.databinding.WatchlistItemBinding
import today.kinema.databinding.WatchlistItemHeaderBinding
import today.kinema.vo.WatchlistMovie

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class WatchlistAdapter(
    private val watchlistITemClickCallback: WatchlistITemViewClickCallback
) : ListAdapter<WatchlistMovie, RecyclerView.ViewHolder>(WatchListDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(watchlists: List<WatchlistMovie>) {
        adapterScope.launch {
            if (watchlists.isNotEmpty()) {
                var dateTitle = watchlists[0].dateTitle
                watchlists[0].header = true

                for (x in 1 until watchlists.size) {
                    if (watchlists[x].dateTitle == dateTitle)
                        watchlists[x].header = false
                    else {
                        watchlists[x].header = true
                        dateTitle = watchlists[x].dateTitle
                    }
                }
            }
            withContext(Dispatchers.Main) {
                submitList(watchlists)
            }
        }
    }

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
                movie = item.movie
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

                movie = item.movie
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
        oldItem.movie == newItem.movie
}


interface WatchlistITemViewClickCallback {
    fun removeFavoriteMovie(watchlistMovie: WatchlistMovie)
    fun navigateTo(view: View, watchlistMovie: WatchlistMovie)
}
