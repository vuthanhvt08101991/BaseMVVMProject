package com.gmail.thanhva.basemvvmproject

import android.app.Application
import com.gmail.thanhva.basemvvmproject.di.appModule
import com.gmail.thanhva.basemvvmproject.di.networkModule
import com.gmail.thanhva.basemvvmproject.di.repositoryModule
import com.gmail.thanhva.basemvvmproject.di.viewModelModule
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 *  Create by thanhva on 18/08/2020
 *  Class MainApplication
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { }

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule, networkModule, repositoryModule, viewModelModule)
        }
    }
}