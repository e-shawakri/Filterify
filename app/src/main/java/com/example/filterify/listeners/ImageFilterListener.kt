package com.example.filterify.listeners

import com.example.filterify.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}