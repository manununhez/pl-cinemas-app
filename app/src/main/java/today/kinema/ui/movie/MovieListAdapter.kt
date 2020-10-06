package today.kinema.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.MovieItemBinding
import today.kinema.vo.Movies

class MovieListAdapter(
    private val movieClickCallback: MovieViewClickCallback
) : ListAdapter<Movies, MovieListAdapter.MovieViewHolder>(Companion) {

    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean =
            oldItem.movie.id == newItem.movie.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(layoutInflater)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = getItem(position)

        holder.binding.apply {
            movie = currentMovie.movie
            movieItemCardView.setOnClickListener {
                movieClickCallback.onClick(it, currentMovie)
            }
            executePendingBindings()
        }
    }
}

interface MovieViewClickCallback {
    fun onClick(cardView: View, movies: Movies)
}
