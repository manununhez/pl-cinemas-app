package today.kinema.ui.movie

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import today.kinema.databinding.MovieItemBinding
import today.kinema.vo.Movie
import today.kinema.vo.WatchlistMovie

class MovieListAdapter(
    private val movieClickCallback: MovieViewClickCallback,
    private val currentWatchlistMovie: LiveData<List<WatchlistMovie>>,
    private val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieListDiffCallback()) {

    private val starredCornerSize = 50f
    private lateinit var context: Context

    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(layoutInflater)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = getItem(position)

        holder.binding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivFavorite.visibility = View.GONE
                currentWatchlistMovie.observe(viewLifecycleOwner, { list ->
                    updateCardViewTopRightCornerSize(imageView, 0.0f)
                    backgroundImageView.visibility = View.GONE
                    list.filter { (it.movie.id == currentMovie.id && it.movie.dateTitle == currentMovie.dateTitle) }
                        .map {
                            backgroundImageView.visibility = View.VISIBLE
                            updateCardViewTopRightCornerSize(imageView, starredCornerSize * 3.0f)
                        }
                })

                setCardViewTopLeftCornerSize(movieItemCardView, imageView, backgroundImageView)
            } else {
                backgroundImageView.visibility = View.GONE
                currentWatchlistMovie.observe(viewLifecycleOwner, { list ->
                    ivFavorite.visibility = View.GONE
                    list.filter { (it.movie.id == currentMovie.id && it.movie.dateTitle == currentMovie.dateTitle) }
                        .map {
                            ivFavorite.visibility = View.VISIBLE
                        }
                })
            }
            movie = currentMovie
            movieItemCardView.setOnClickListener {
                movieClickCallback.onClick(it, currentMovie)
            }
            executePendingBindings()
        }

    }

    private fun setCardViewTopLeftCornerSize(
        cardView: MaterialCardView,
        imageView: ShapeableImageView,
        backgroundImageView: ShapeableImageView
    ) {
        cardView.apply {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopLeftCornerSize(starredCornerSize)
                .setBottomRightCornerSize(starredCornerSize)
                .build()
        }

        imageView.apply {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopLeftCornerSize(starredCornerSize)
                .build()
        }

        backgroundImageView.apply {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopLeftCornerSize(starredCornerSize)
                .build()
        }
    }

    private fun updateCardViewTopRightCornerSize(
        imageView: ShapeableImageView,
        size: Float
    ) {
        imageView.apply {
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopRightCornerSize(size)
                .build()
        }
    }
}

class MovieListDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        (oldItem.id == newItem.id && oldItem.dateTitle == newItem.dateTitle)

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}

interface MovieViewClickCallback {
    fun onClick(view: View, movies: Movie)
}
