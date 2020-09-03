package com.gmail.thanhva.basemvvmproject.ui.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.listener.SplashCallBack
import com.gmail.thanhva.basemvvmproject.ui.base.BaseActivity
import com.gmail.thanhva.basemvvmproject.ui.screen.main.MainContainerFragment
import com.gmail.thanhva.basemvvmproject.ui.screen.splash.SplashFragment
import com.gmail.thanhva.basemvvmproject.utils.getTopFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel>(), SplashCallBack {

    override val viewModel: MainViewModel by viewModel()

    private val shareViewModel: ShareViewModel by viewModel()

    private var mainContainerFragment: MainContainerFragment? = null

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
        MainContainerFragment.newInstance().let { mainFragment ->
            mainContainerFragment = mainFragment
            addFragment(
                fragment = mainFragment,
                tag = MainContainerFragment::class.java.simpleName,
                enterAnim = 0,
                exitAnim = 0
            )
        }
    }

    override fun onBackPressed() {
        when (val topFragment = getTopFragment(supportFragmentManager)) {
            is MainContainerFragment -> {
                topFragment.onBackListener()
            }

            else -> popFragment()
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

    private fun popFragment() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    private fun popAllFragment(): Boolean {
        val count = supportFragmentManager.backStackEntryCount
        var lastFragment: Fragment? = null
        if (count > 0) {
            val backEntry = supportFragmentManager.getBackStackEntryAt(0)
            lastFragment = supportFragmentManager.findFragmentByTag(backEntry.name)
        }
        if (count > 1) {
            for (i in 0 until count - 1) {
                supportFragmentManager.popBackStack()
            }
        }

        return if (lastFragment is MainContainerFragment) {
            mainContainerFragment = lastFragment
            true
        } else {
            if (lastFragment != null) {
                supportFragmentManager.popBackStack()
            }
            MainContainerFragment.newInstance().let { main ->
                mainContainerFragment = main
                addFragment(
                    fragment = main,
                    tag = MainContainerFragment::class.java.simpleName,
                    enterAnim = 0,
                    exitAnim = 0
                )
            }
            true
        }
    }
}
