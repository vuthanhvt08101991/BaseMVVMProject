package com.gmail.thanhva.basemvvmproject.ui.base

import androidx.lifecycle.MutableLiveData

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseWebViewViewModel
 */
abstract class BaseWebViewViewModel : BaseViewModel() {

    var obsUrlWebView = MutableLiveData<String>()
    var obsTextTitle = MutableLiveData<String>().apply { value = "" }
    var obsProgressBar = MutableLiveData<Int>().apply { value = 0 }

    fun onLoadDataSuccess(webUrl: String?) {
        obsUrlWebView.value = webUrl
    }
}