package com.example.filterify.repo

import android.graphics.Bitmap
import java.io.File

interface SavedImageRepo {
    suspend fun loadSavedImages(): List<Pair<File, Bitmap>>?
}