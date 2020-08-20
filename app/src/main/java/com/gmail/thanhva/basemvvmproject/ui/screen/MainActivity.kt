package com.gmail.thanhva.basemvvmproject.ui.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.listener.OnBackListener
import com.gmail.thanhva.basemvvmproject.listener.SplashCallBack
import com.gmail.thanhva.basemvvmproject.ui.base.BaseActivity
import com.gmail.thanhva.basemvvmproject.ui.screen.splash.SplashFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel>(), OnBackListener, SplashCallBack {

    private val onBackListeners: MutableList<OnBackListener> = ArrayList()

    override val viewModel: MainViewModel by viewModel()

    private val shareViewModel: ShareViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(
            fragment = SplashFragment().apply {
                setSplashCallBack(this@MainActivity)
            },
            tag = SplashFragment::class.java.simpleName,
            enterAnim = 0,
            exitAnim = 0
        )
    }

    override fun onSplashSuccess() {
        Log.d("---------->", "MainActivity: onSplashSuccess: ")
    }

    override fun onBackPressed() {
        onBackListener()
    }

    override fun onBackListener() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    private fun addFragment(
        fragment: Fragment, tag: String,
        enterAnim: Int = R.anim.enter_from_right,
        exitAnim: Int = R.anim.exit_to_right
    ) {
        supportFragmentManager.beginTransaction().apply {
            if (enterAnim != 0 || exitAnim != 0) {
                setCustomAnimations(enterAnim, 0, 0, exitAnim)
            }
            add(R.id.frame_main_activity, fragment, tag)
            addToBackStack(tag)
        }.commitAllowingStateLoss()
    }
}
