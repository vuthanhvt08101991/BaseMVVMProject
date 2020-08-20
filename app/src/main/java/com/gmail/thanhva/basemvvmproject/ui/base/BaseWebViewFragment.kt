package com.gmail.thanhva.basemvvmproject.ui.base

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.http.SslError
import android.webkit.*
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gmail.thanhva.basemvvmproject.R

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseWebViewFragment
 */
abstract class BaseWebViewFragment<ViewBinding : ViewDataBinding, ViewModel : BaseWebViewViewModel> :
    BaseFragmentBinding<ViewBinding, ViewModel>() {

    abstract val webView: WebView
    private var urlWebView = ""

    protected var isShowLoadingWebView = true

    override fun initData() {
        settingWebView()
        // Load url webview
        // viewModel.loadData(urlWebView)
    }

    @CallSuper
    override fun observeField() {
        with(viewModel) {
            obsUrlWebView.observe(viewLifecycleOwner, Observer { urlWeb ->
                loadContentWebView(urlWeb)
            })
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected fun settingWebView() {
        webView.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptEnabled = true
            // Update user agent
            // userAgentString += BuildConfig.VERSION_NAME
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedHttpAuthRequest(
                view: WebView?,
                handler: HttpAuthHandler?,
                host: String?,
                realm: String?
            ) {
                // Add authentication if need
                // handler?.proceed(BuildConfig.USER_NAME_AUTHEN, BuildConfig.PASSWORD_AUTHEN)
            }

            @Suppress("DEPRECATION")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Handle if need
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Handle if need
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //Handle data in cookie
                val cookies = CookieManager.getInstance().getCookie(url)
                handleDataFromCookies(cookies)

                onLoadWebViewSuccess()
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                @Suppress("DEPRECATION")
                super.onReceivedError(view, errorCode, description, failingUrl)
                viewModel.hideLoading()
                onLoadWebViewFail()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                viewModel.hideLoading()
                onLoadWebViewFail()
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                viewModel.hideLoading()
                onLoadWebViewFail()
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                viewModel.hideLoading()
                onLoadWebViewFail()
            }
        }
    }

    @CallSuper
    open fun loadContentWebView(urlWeb: String?) {
        if (isShowLoadingWebView) {
            viewModel.showLoading()
        }

        webView.loadUrl(urlWeb, HashMap<String, String>().apply {
            // Add header
            // this[KEY] = value
        })

        webView.webChromeClient = object : WebChromeClient() {
            override fun getDefaultVideoPoster(): Bitmap? {
                return super.getDefaultVideoPoster()?.let { it } ?: run {
                    BitmapFactory.decodeResource(context?.resources, R.drawable.preview_video)
                }
            }

            override fun onProgressChanged(view: WebView, progress: Int) {
                viewModel.obsProgressBar.value = progress

                if (progress >= 100) {
                    viewModel.apply {
                        isLoading.value = false
                    }
                }
            }

            override fun onReceivedTitle(webView: WebView?, title: String?) {
                super.onReceivedTitle(webView, title)
                updateTitleWebView(title)
            }
        }
    }

    open fun onLoadWebViewSuccess() {}

    open fun onLoadWebViewFail() {}

    private fun handleDataFromCookies(cookies: String?) {
        if (cookies.isNullOrEmpty()) return
        val listCookies = cookies.split(";")
        for (item in listCookies) {
            // do something
        }
    }

    fun updateTitleWebView(title: String?) {
        title?.run {
            viewModel.obsTextTitle.value = this
        }
    }
}