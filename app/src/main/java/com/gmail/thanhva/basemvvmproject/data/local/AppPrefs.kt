package com.gmail.thanhva.basemvvmproject.data.local

import com.gmail.thanhva.basemvvmproject.BuildConfig

/**
 *  Create by thanhva on 18/08/2020
 *  Class AppPrefs
 */
class AppPrefs constructor(private val sharedPrefsApi: SharedPrefsApi) : PrefHelper {

    override fun clear() = sharedPrefsApi.clear()

    override fun remove(key: String) = sharedPrefsApi.remove(key)

    override fun getBaseUrl() = sharedPrefsApi.get(SharePrefsKey.BASE_URL, BuildConfig.API_URL)

    override fun setBaseUrl(baseUrl: String) = sharedPrefsApi.set(SharePrefsKey.BASE_URL, baseUrl)
}