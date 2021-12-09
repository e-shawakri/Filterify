package com.example.filterify.di

import com.example.filterify.viewmodel.EditImageViewModel
import com.example.filterify.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepo = get()) }
    viewModel { SavedImagesViewModel(savedImageRepo = get()) }
}