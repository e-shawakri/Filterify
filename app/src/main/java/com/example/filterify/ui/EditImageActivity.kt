package com.example.filterify.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.filterify.adapters.ImageFiltersAdapter
import com.example.filterify.data.ImageFilter
import com.example.filterify.databinding.ActivityEditImageBinding
import com.example.filterify.listeners.ImageFilterListener
import com.example.filterify.utilities.displayToast
import com.example.filterify.utilities.show
import com.example.filterify.viewmodel.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        setupObserver()
        prepareImagePreview()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->

                // original bitmap
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }

            } ?: kotlin.run {
                dataState.error?.let {
                    displayToast(it)
                    onBackPressed()
                }
            }
        })

        viewModel.imageFiltersUiState.observe(this, {
            val imageFiltersDataState = it ?: return@observe
            binding.loadFiltersPreviewProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }

            } ?: kotlin.run {
                imageFiltersDataState.error?.let {
                    displayToast(it)
                }
            }
        })

        filteredBitmap.observe(this, { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredImageUiState.observe(this, {
            val saveFilteredImageDataState = it ?: return@observe
            if(saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.INVISIBLE
                binding.saveImageProgressBar.visibility = View.VISIBLE
            } else {
                binding.imageSave.visibility = View.VISIBLE
                binding.saveImageProgressBar.visibility = View.GONE
            }

            saveFilteredImageDataState.uri?.let { savedImage ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageBitmap ->
                    filteredImageBitmap.putExtra(KEY_FILTERED_IMAGE_URI, savedImage)
                    startActivity(filteredImageBitmap)
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let{
                    displayToast(it)
                }
            }
        })
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let {
            viewModel.prepareImagePreview(it)
        }
    }

    private fun setListener() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let {
                viewModel.saveFilterImage(it)
            }
        }

        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}