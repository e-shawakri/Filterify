package com.example.filterify.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filterify.repo.SavedImageRepo
import com.example.filterify.utilities.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImageRepo: SavedImageRepo) : ViewModel() {

    private val savedImagesDataState = MutableLiveData<SavedImagesDataState>()
    val savedImagesUiState : LiveData<SavedImagesDataState> get() = savedImagesDataState

    fun loadImages(){
        Coroutines.io {
            runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImageRepo.loadSavedImages()
            }.onSuccess { savedImage ->
                if (savedImage.isNullOrEmpty()){
                    emitSavedImagesUiState(error = "No image found")
                } else {
                    emitSavedImagesUiState(savedImages = savedImage)
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.message.toString())
            }
        }
    }
    private fun emitSavedImagesUiState(
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ){
        val dataState = SavedImagesDataState(isLoading, savedImages, error)
        savedImagesDataState.postValue(dataState)
    }

    data class SavedImagesDataState(
        val isLoading: Boolean,
        val savedImages: List<Pair<File, Bitmap>>?,
        val error: String?
    )

}