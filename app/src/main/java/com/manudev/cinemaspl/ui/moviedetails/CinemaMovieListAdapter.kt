package com.manudev.cinemaspl.ui.moviedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.databinding.CinemaItemBinding
import com.manudev.cinemaspl.vo.Cinema

class CinemaMovieListAdapter(
    private val items: List<Cinema>,
    private val cinemaClickCallback: CinemaViewClickCallback
) :
    RecyclerView.Adapter<CinemaMovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CinemaItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], cinemaClickCallback)

    class ViewHolder(val binding: CinemaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Cinema, cinemaClickCallback: CinemaViewClickCallback) {
            binding.cinema = item
            binding.cinemaItemCardView.setOnClickListener {
                cinemaClickCallback.onClick(item)
            }
            binding.executePendingBindings()
        }
    }
}

interface CinemaViewClickCallback {
    fun onClick(cinema: Cinema)
}
