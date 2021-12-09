package com.example.filterify.di

import com.example.filterify.repo.EditImageRepo
import com.example.filterify.repo.EditImageRepoImplementation
import com.example.filterify.repo.SavedImageRepo
import com.example.filterify.repo.SavedImagesRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    factory<EditImageRepo> { EditImageRepoImplementation(androidContext()) }
    factory<SavedImageRepo> { SavedImagesRepoImpl(androidContext()) }
}