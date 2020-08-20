package com.gmail.thanhva.basemvvmproject.data.local

/**
 *  Create by thanhva on 18/08/2020
 *  Class PrefHelper
 */
interface PrefHelper {

    fun remove(key: String)
    fun clear()

    fun getBaseUrl(): String
    fun setBaseUrl(baseUrl: String)
}