package com.example.filterify.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filterify.databinding.ItemContainerSavedImageBinding
import com.example.filterify.listeners.SavedImageListener
import java.io.File

class SavedImageAdapter(
    private val savedImages: List<Pair<File, Bitmap>>,
    private val savedImageLister: SavedImageListener
    ) : RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>(){

    inner class SavedImageViewHolder(val binding: ItemContainerSavedImageBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){
                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener{
                    savedImageLister.onImageClicked(first)
                }
            }
        }
    }

    override fun getItemCount() = savedImages.size


}