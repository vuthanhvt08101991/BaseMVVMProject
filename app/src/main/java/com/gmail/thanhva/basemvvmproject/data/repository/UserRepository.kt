package com.gmail.thanhva.basemvvmproject.data.repository

/**
 *  Create by thanhva on 18/08/2020
 *  Class UserRepository
 */
interface UserRepository {

    fun getBaseUrl(): String

    fun setBaseUrl(baseUrl: String)
}