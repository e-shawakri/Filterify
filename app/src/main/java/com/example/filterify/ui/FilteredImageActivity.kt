package com.example.filterify.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.filterify.R
import com.example.filterify.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private lateinit var binding: ActivityFilteredImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayFilteredImage()
        setListeners()
    }

    private fun displayFilteredImage(){
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let {
            fileUri = it
            binding.imageFilteredView.setImageURI(it)
        }
    }

    private fun setListeners(){
        binding.fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

}