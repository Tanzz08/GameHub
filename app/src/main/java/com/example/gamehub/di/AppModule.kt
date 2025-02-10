package com.example.gamehub.di

import com.example.core.domain.usecase.GameHubInteractor
import org.koin.dsl.module
import com.example.core.domain.usecase.GameHubUseCase
import com.example.gamehub.ui.detail.DetailViewModel
import com.example.gamehub.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

val useCaseModule = module {
    factory<GameHubUseCase> { GameHubInteractor(get()) }
}

val viewModelModule = module {
    viewModel {HomeViewModel(get())}
    viewModel { DetailViewModel(get()) }
}


