package com.gmail.thanhva.basemvvmproject.ui.screen.first_screen.wrapper

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.data.constant.StateFragment
import com.gmail.thanhva.basemvvmproject.databinding.FragmentFirstWrapperBinding
import com.gmail.thanhva.basemvvmproject.ui.base.BaseFragmentWrapper
import com.gmail.thanhva.basemvvmproject.ui.screen.first_screen.FirstFragment
import com.gmail.thanhva.basemvvmproject.utils.addFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  Create by thanhva on 28/08/2020
 *  Class FirstWrapperFragment
 */
class FirstWrapperFragment : BaseFragmentWrapper<FragmentFirstWrapperBinding, FirstWrapperViewModel>() {

    override val viewModel: FirstWrapperViewModel by viewModel()

    override val layoutId = R.layout.fragment_first_wrapper

    private var firstFragment: FirstFragment? = null

    private var stateFragment = StateFragment.ON_START

    private var lifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            Log.d("---------->", "FirstWrapperFragment: onResume: ")
            stateFragment = StateFragment.ON_RESUME
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            Log.d("---------->", "FirstWrapperFragment: onPause: ")
            stateFragment = StateFragment.ON_PAUSE
        }
    }

    override fun getTagFragment() = FirstWrapperFragment::class.java.simpleName

    override fun beforeAddContent() {
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun initView() {

    }

    override fun initData() {
        addFirstFragment()
    }

    override fun observeField() {

    }

    private fun addFirstFragment() {
        if (!this.isAdded) return
        FirstFragment.newInstance().let { fragment ->
            firstFragment = fragment
            addFragment(
                childFragmentManager,
                fragment,
                R.id.frame_first_wrapper,
                fragment.getTagFragment(),
                true,
                0,
                0
            )
        }
    }

    fun isUserVisibleHint() = stateFragment == StateFragment.ON_RESUME

    companion object {
        fun newInstance() = FirstWrapperFragment()
    }
}