package com.gmail.thanhva.basemvvmproject.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 *  Create by thanhva on 18/08/2020
 *  Class AppModule
 */
val appModule = module {
    single { androidApplication().resources }
}