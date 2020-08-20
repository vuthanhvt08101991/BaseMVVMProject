package com.gmail.thanhva.basemvvmproject.ui.screen.splash

import android.os.Handler
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.databinding.FragmentSplashBinding
import com.gmail.thanhva.basemvvmproject.listener.SplashCallBack
import com.gmail.thanhva.basemvvmproject.ui.base.BaseFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  Create by thanhva on 19/08/2020
 *  Class SplashFragment
 */
class SplashFragment: BaseFragmentBinding<FragmentSplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModel()

    private var splashCallBack: SplashCallBack? = null

    override val layoutId: Int
        get() = R.layout.fragment_splash

    override fun getTagFragment() = SplashFragment::class.java.simpleName

    override fun initView() {

    }

    override fun initData() {

    }

    override fun observeField() {

    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            splashCallBack?.onSplashSuccess()
        }, 1000)
    }

    fun setSplashCallBack(listener: SplashCallBack?) {
        splashCallBack = listener
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}