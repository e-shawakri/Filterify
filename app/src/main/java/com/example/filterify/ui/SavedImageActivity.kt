package com.example.filterify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.filterify.adapters.SavedImageAdapter
import com.example.filterify.databinding.ActivitySavedImageBinding
import com.example.filterify.listeners.SavedImageListener
import com.example.filterify.utilities.displayToast
import com.example.filterify.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class SavedImageActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var binding: ActivitySavedImageBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setListeners()
        viewModel.loadImages()
    }

    private fun setupObservers() {
        viewModel.savedImagesUiState.observe(this, {
            val savedImagesDataState = it ?: return@observe
            binding.savedImageProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE

            savedImagesDataState.savedImages?.let { savedImages ->
                SavedImageAdapter(savedImages, this).also { adapter ->
                    with(binding.savedImagesRecyclerView) {
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }

            } ?: run {
                savedImagesDataState.error?.let {
                    displayToast(it)
                }
            }

        })
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filteredImageIntent)
        }
    }
}