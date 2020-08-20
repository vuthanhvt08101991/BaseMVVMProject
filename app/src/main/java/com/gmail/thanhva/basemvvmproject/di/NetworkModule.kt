package com.gmail.thanhva.basemvvmproject.di

import android.content.Context
import com.gmail.thanhva.basemvvmproject.BuildConfig
import com.gmail.thanhva.basemvvmproject.data.constant.Constants.APP_VERSION_KEY
import com.gmail.thanhva.basemvvmproject.data.remote.ApiService
import com.gmail.thanhva.basemvvmproject.data.remote.RxErrorHandlingFactory
import com.gmail.thanhva.basemvvmproject.utils.getBaseUrl
import okhttp3.Cache
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *  Create by thanhva on 18/08/2020
 *  Class NetworkModule
 */
val networkModule = module {
    single { createOkHttpCache(get()) }
    single(named("logging")) { createLoggingInterceptor() }
    single(named("header")) { createHeaderInterceptor() }
    single { createOkHttpClient(get(), get(named("logging")), get(named("header"))) }
    single { createAppRetrofit(get(), get()) }
    single { createApiService(get()) }
    single { RxErrorHandlingFactory() }
}

const val TIMEOUT = 30

fun createOkHttpCache(context: Context): Cache =
    Cache(context.cacheDir, (10 * 1024 * 1024).toLong())

fun createLoggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

fun createHeaderInterceptor(): Interceptor =
    Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder().apply {
            url(request.url().newBuilder().build())
            header("Authorization", Credentials.basic(BuildConfig.USER_NAME_AUTHEN, BuildConfig.PASSWORD_AUTHEN))
            header(APP_VERSION_KEY, BuildConfig.VERSION_NAME)
            header("Accept", "*/*")
            method(request.method(), request.body())
        }.build()
        val response = chain.proceed(newRequest)
        response
    }

fun createOkHttpClient(
    cache: Cache,
    logging: Interceptor,
    header: Interceptor
): OkHttpClient = OkHttpClient.Builder().apply {
    connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
    readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
    addInterceptor(header)
    addInterceptor(logging)
    cache(cache)
}.build()

fun createAppRetrofit(
    okHttpClient: OkHttpClient,
    rxErrorHandlingFactory: RxErrorHandlingFactory
): Retrofit {
    return Retrofit.Builder().apply {
        addCallAdapterFactory(rxErrorHandlingFactory)
        addConverterFactory(GsonConverterFactory.create())
        baseUrl(getBaseUrl())
        client(okHttpClient)
    }.build()
}


fun createApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

fun createApiServiceWithBaseUrl(baseUrl: String): ApiService {
    val context: Context by KoinJavaComponent.inject(Context::class.java)
    val okHttpClient = createOkHttpClient(
        createOkHttpCache(context),
        createLoggingInterceptor(),
        createHeaderInterceptor()
    )
    val rxErrorHandlingFactory = RxErrorHandlingFactory()

    return Retrofit.Builder().apply {
        addCallAdapterFactory(rxErrorHandlingFactory)
        addConverterFactory(GsonConverterFactory.create())
        baseUrl(baseUrl)
        client(okHttpClient)
    }.build().create(ApiService::class.java)
}