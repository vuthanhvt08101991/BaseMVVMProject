package com.gmail.thanhva.basemvvmproject.di

import com.gmail.thanhva.basemvvmproject.ui.screen.ShareViewModel
import com.gmail.thanhva.basemvvmproject.ui.screen.first_screen.FirstViewModel
import com.gmail.thanhva.basemvvmproject.ui.screen.first_screen.wrapper.FirstWrapperViewModel
import com.gmail.thanhva.basemvvmproject.ui.screen.main.MainContainerViewModel
import com.gmail.thanhva.basemvvmproject.ui.screen.second_screen.SecondViewModel
import com.gmail.thanhva.basemvvmproject.ui.screen.second_screen.wrapper.SecondWrapperViewModel
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
    viewModel { MainContainerViewModel() }
    viewModel { FirstWrapperViewModel() }
    viewModel { FirstViewModel() }
    viewModel { SecondWrapperViewModel() }
    viewModel { SecondViewModel() }
}