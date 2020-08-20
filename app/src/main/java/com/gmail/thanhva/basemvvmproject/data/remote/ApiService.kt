package com.gmail.thanhva.basemvvmproject.data.remote

import com.gmail.thanhva.basemvvmproject.data.model.Item
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *  Create by thanhva on 18/08/2020
 *  Class ApiService
 */
interface ApiService {

    @GET("api/{id}")
    fun getDefaultApi(@Path("id") id: Long): Single<Item>
}