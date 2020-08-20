package com.gmail.thanhva.basemvvmproject.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.thanhva.basemvvmproject.data.constant.ErrorType
import com.gmail.thanhva.basemvvmproject.data.remote.BaseException
import com.gmail.thanhva.basemvvmproject.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.java.KoinJavaComponent
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *  Create by thanhva on 18/08/2020
 *  Class BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {

    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isBackClick = SingleLiveEvent<Boolean>().apply { value = false }
    val baseErrorEvent = SingleLiveEvent<ErrorType>()
    var bodyMessage: String? = null

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    open fun onLoadFail(throwable: Throwable) {
        when (throwable.cause) {
            is UnknownHostException -> {
                baseErrorEvent.value = ErrorType.NO_INTERNET
            }
            is SocketTimeoutException -> {
                baseErrorEvent.value = ErrorType.CONNECT_TIME_OUT
            }
            else -> {
                val context: Context by KoinJavaComponent.inject(Context::class.java)
                when (throwable) {
                    is BaseException -> {
                        when (throwable.httpCode) {
                            HttpURLConnection.HTTP_UNAVAILABLE -> {
                                bodyMessage = throwable.serverErrorResponse?.message
                                baseErrorEvent.value = ErrorType.UNAVAILABLE
                            }

                            HttpURLConnection.HTTP_BAD_REQUEST -> {
                                if (throwable.serverErrorResponse?.error == "FORCE_UPDATE") {
                                    baseErrorEvent.value = ErrorType.FORCE_UPDATE
                                } else {
                                    baseErrorEvent.value = ErrorType.UNKNOWN
                                }
                            }
                            else -> {
                                baseErrorEvent.value = ErrorType.UNKNOWN
                            }
                        }
                    }
                    else -> {
                        baseErrorEvent.value = ErrorType.UNKNOWN
                    }
                }
            }
        }
        hideLoading()
    }

    fun showLoading() {
        isLoading.postValue(true)
    }

    fun hideLoading() {
        isLoading.postValue(false)
    }

    fun clearDisposable() {
        compositeDisposable.clear()
    }

    fun onBack() {
        isBackClick.value = true
    }
}