package com.gmail.thanhva.basemvvmproject.ui.screen.second_screen.wrapper

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.data.constant.StateFragment
import com.gmail.thanhva.basemvvmproject.databinding.FragmentSecondWrapperBinding
import com.gmail.thanhva.basemvvmproject.ui.base.BaseFragmentWrapper
import com.gmail.thanhva.basemvvmproject.ui.screen.second_screen.SecondFragment
import com.gmail.thanhva.basemvvmproject.utils.addFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  Create by thanhva on 28/08/2020
 *  Class SecondWrapperFragment
 */
class SecondWrapperFragment : BaseFragmentWrapper<FragmentSecondWrapperBinding, SecondWrapperViewModel>() {

    override val viewModel: SecondWrapperViewModel by viewModel()

    override val layoutId = R.layout.fragment_second_wrapper

    private var secondFragment: SecondFragment? = null

    private var stateFragment = StateFragment.ON_START

    private var lifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            Log.d("---------->", "SecondWrapperFragment: onResume: ")
            stateFragment = StateFragment.ON_RESUME
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            Log.d("---------->", "SecondWrapperFragment: onPause: ")
            stateFragment = StateFragment.ON_PAUSE
        }
    }

    override fun getTagFragment() = SecondWrapperFragment::class.java.simpleName

    override fun beforeAddContent() {
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun initView() {

    }

    override fun initData() {
        addSecondFragment()
    }

    override fun observeField() {

    }

    private fun addSecondFragment() {
        if (!this.isAdded) return
        SecondFragment.newInstance().let { fragment ->
            secondFragment = fragment
            addFragment(
                childFragmentManager,
                fragment,
                R.id.frame_second_wrapper,
                fragment.getTagFragment(),
                true,
                0,
                0
            )
        }
    }

    fun isUserVisibleHint() = stateFragment == StateFragment.ON_RESUME

    companion object {
        fun newInstance() = SecondWrapperFragment()
    }
}