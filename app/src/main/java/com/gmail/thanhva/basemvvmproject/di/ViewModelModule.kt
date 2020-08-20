package com.gmail.thanhva.basemvvmproject.di

import com.gmail.thanhva.basemvvmproject.ui.screen.ShareViewModel
import com.gmail.thanhva.basemvvmproject.ui.screen.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *  Create by thanhva on 18/08/2020
 *  Class ViewModelModule
 */
val viewModelModule = module {
    viewModel { ShareViewModel() }
    viewModel { SplashViewModel() }
}