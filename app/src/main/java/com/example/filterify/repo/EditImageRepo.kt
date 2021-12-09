package com.example.filterify.repo

import android.graphics.Bitmap
import android.net.Uri
import com.example.filterify.data.ImageFilter

interface EditImageRepo {
    suspend fun EditImageRepo(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri?
}