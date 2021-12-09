package com.example.filterify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.filterify.R
import com.example.filterify.data.ImageFilter
import com.example.filterify.databinding.ItemContainerFilterBinding
import com.example.filterify.listeners.ImageFilterListener

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
    ): RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    private var selectedFilterPosition = 0
    private var previousSelectedFilterPosition = 0

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener{
                    if (holder.adapterPosition != selectedFilterPosition){
                        imageFilterListener.onFilterSelected(this)
                        previousSelectedFilterPosition = selectedFilterPosition
                        selectedFilterPosition = holder.adapterPosition
                        with(this@ImageFiltersAdapter){
                            notifyItemChanged(previousSelectedFilterPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }

                    }
                }
            }
            binding.textFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.textFilterName.context,
                    if (selectedFilterPosition == position)
                        R.color.primaryDark
                    else
                        R.color.primaryText
                )
            )
        }
    }

    override fun getItemCount(): Int = imageFilters.size
}