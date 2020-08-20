package com.gmail.thanhva.basemvvmproject.di

import com.gmail.thanhva.basemvvmproject.data.local.AppPrefs
import com.gmail.thanhva.basemvvmproject.data.local.PrefHelper
import com.gmail.thanhva.basemvvmproject.data.local.SharedPrefsApi
import com.gmail.thanhva.basemvvmproject.data.repository.UserRepository
import com.gmail.thanhva.basemvvmproject.data.repository.impl.UserRepositoryImpl
import com.google.gson.Gson
import org.koin.dsl.module

/**
 *  Create by thanhva on 18/08/2020
 *  Class RepositoryModule
 */
val repositoryModule = module {
    single<PrefHelper> { AppPrefs(get()) }
    single { Gson() }
    single { SharedPrefsApi(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}