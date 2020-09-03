package com.gmail.thanhva.basemvvmproject.ui.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.gmail.thanhva.basemvvmproject.listener.OnBackListener

/**
 *  Create by thanhva on 28/08/2020
 *  Class BaseFragmentWrapper
 */
abstract class BaseFragmentWrapper<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    BaseFragmentBinding<ViewBinding, ViewModel>(), OnBackListener {

    override fun onBackListener() {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
        } else {
            activity?.finish()
        }
    }

    fun popAllFragment(): Fragment? {
        if (!this.isAdded) return null
        val count = childFragmentManager.backStackEntryCount
        if (count > 1) {
            for (i in count downTo 2) {
                childFragmentManager.popBackStack()
            }
        }
        var lastFragment: Fragment? = null
        if (count > 0) {
            val backEntry = childFragmentManager.getBackStackEntryAt(0)
            lastFragment = childFragmentManager.findFragmentByTag(backEntry.name)
        }
        return lastFragment
    }
}