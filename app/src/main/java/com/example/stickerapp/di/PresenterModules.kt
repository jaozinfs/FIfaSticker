package com.example.stickerapp.di

import com.example.stickerapp.presenter.sticker.StickersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presenterModules = module {
    viewModel { StickersViewModel(get(), get(), get()) }
}