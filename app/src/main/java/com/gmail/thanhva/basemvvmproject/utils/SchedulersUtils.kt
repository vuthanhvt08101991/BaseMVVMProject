package com.gmail.thanhva.basemvvmproject.utils

import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *  Create by thanhva on 19/08/2020
 *  Class SchedulersUtils
 */
object SchedulersUtils {
    fun <T> applyAsyncSchedulersSingle(): SingleTransformer<T, T> {
        return SingleTransformer { func ->
            func.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun applyAsyncSchedulersCompletable(): CompletableTransformer {
        return CompletableTransformer { func ->
            func.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyAsyncSchedulersFlowable(): FlowableTransformer<T, T> {
        return FlowableTransformer { func ->
            func.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}