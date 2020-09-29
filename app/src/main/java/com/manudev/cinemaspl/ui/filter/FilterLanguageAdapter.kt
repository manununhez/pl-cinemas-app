package com.manudev.cinemaspl.ui.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.manudev.cinemaspl.databinding.FilterItemMultipleChoiceBinding
import com.manudev.cinemaspl.vo.FilterAttribute

class FilterLanguageAdapter(
    private val items: List<String>,
    private val currentAttribute: LiveData<FilterAttribute>,
    private val lifecycleOwner: LifecycleOwner,
    private val languageViewClickCallback: FilterLanguageViewClickCallback
) :
    RecyclerView.Adapter<FilterLanguageAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemMultipleChoiceBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            items[position],
            currentAttribute,
            lifecycleOwner,
            languageViewClickCallback
        )

    class ViewHolder(val binding: FilterItemMultipleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: String,
            currentAttribute: LiveData<FilterAttribute>,
            viewLifecycleOwner: LifecycleOwner,
            languageViewClickCallback: FilterLanguageViewClickCallback
        ) {
            //TODO trying not to use LiveData here!
            currentAttribute.observe(viewLifecycleOwner, {
                binding.checkBox.isChecked = it.language.contains(item)

                binding.tvBackgroundOVerlay.visibility = if(binding.checkBox.isChecked) View.VISIBLE else View.GONE
            })

            binding.location = item
            binding.checkBox.setOnClickListener {
                languageViewClickCallback.onClick(item)
            }
            binding.executePendingBindings()
        }
    }
}

interface FilterLanguageViewClickCallback {
    fun onClick(language: String)
}
