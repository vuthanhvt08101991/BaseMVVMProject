package com.gmail.thanhva.basemvvmproject.data.repository.impl

import com.gmail.thanhva.basemvvmproject.data.local.PrefHelper
import com.gmail.thanhva.basemvvmproject.data.remote.ApiService
import com.gmail.thanhva.basemvvmproject.data.repository.UserRepository

/**
 *  Create by thanhva on 18/08/2020
 *  Class UserRepositoryImpl
 */
class UserRepositoryImpl(
    private val apiService: ApiService,
    private val prefHelper: PrefHelper
) : UserRepository {

    override fun getBaseUrl(): String = prefHelper.getBaseUrl()

    override fun setBaseUrl(baseUrl: String) {
        prefHelper.setBaseUrl(baseUrl)
    }
}