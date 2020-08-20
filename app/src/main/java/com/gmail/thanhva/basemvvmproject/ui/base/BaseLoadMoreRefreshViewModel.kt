package com.gmail.thanhva.basemvvmproject.ui.base

import androidx.lifecycle.MutableLiveData
import com.gmail.thanhva.basemvvmproject.data.constant.Constants

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseLoadMoreRefreshViewModel
 */
abstract class BaseLoadMoreRefreshViewModel<Item> : BaseViewModel() {

    val isRefreshing = MutableLiveData<Boolean>().apply { value = false }
    val isLoadMore = MutableLiveData<Boolean>()
    val isLoadMoreFail = MutableLiveData<Boolean>()
    var page = getDefaultFirstPage()
    var hasLoadMore = false
    abstract var isApiLoadMore: Boolean
    val listItem = MutableLiveData<MutableList<Item>>()

    abstract fun loadData()

    fun firstLoad(isShowLoading: Boolean) {
        if (isShowLoading) isLoading.value = true
        page = getDefaultFirstPage()
        loadData()
    }

    open fun refreshData() {
        refreshData(true)
    }

    open fun refreshData(isShowRefreshView: Boolean) {
        if (isRefreshing.value == true || isLoading.value == true) return
        isRefreshing.value = isShowRefreshView
        page = getDefaultFirstPage()
        loadData()
    }

    fun loadMore() {
        if (!hasLoadMore || isLoading.value == true || isLoadMore.value == true || isRefreshing.value == true) return
        page++
        isLoadMore.value = true
        loadMoreData()
    }

    /**
     * override if need call other api
     */
    open fun loadMoreData() = loadData()

    /**
     * override if first page is not 1
     */
    open fun getDefaultFirstPage() = Constants.DEFAULT_FIRST_PAGE

    open fun getPageSize() = Constants.DEFAULT_FIRST_PAGE

    open fun onLoadSuccess(items: MutableList<Item>?) {
        listItem.value = items
        stopAnimation()
        hasLoadMore = isApiLoadMore && items?.size ?: 0 >= getPageSize()
    }

    override fun onLoadFail(throwable: Throwable) {
        super.onLoadFail(throwable)
        stopAnimation()
        if (hasLoadMore) {
            page--
            isLoadMoreFail.value = true
        }
    }

    fun stopAnimation() {
        isLoading.value = false
        isRefreshing.value = false
        isLoadMore.value = false
    }
}