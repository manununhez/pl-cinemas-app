package today.kinema.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import today.kinema.databinding.MovieItemBinding
import today.kinema.vo.Movie

class MovieListAdapter(
    private val movieClickCallback: MovieViewClickCallback
) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieListDiffCallback()) {

    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(layoutInflater)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = getItem(position)

        holder.binding.apply {
            movie = currentMovie
            movieItemCardView.setOnClickListener {
                movieClickCallback.onClick(it, currentMovie)
            }
            executePendingBindings()
        }
    }
}

class  MovieListDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        (oldItem.id == newItem.id && oldItem.dateTitle == newItem.dateTitle)

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}

interface MovieViewClickCallback {
    fun onClick(view: View, movies: Movie)
}
