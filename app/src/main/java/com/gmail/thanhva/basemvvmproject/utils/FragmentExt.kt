package com.gmail.thanhva.basemvvmproject.utils

import android.annotation.SuppressLint
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.gmail.thanhva.basemvvmproject.R

/**
 *  Create by thanhva on 19/08/2020
 *  Class FragmentExt
 */
fun Fragment.addFragment(
    fragmentManager: FragmentManager,
    fragment: Fragment,
    containerViewId: Int,
    TAG: String?,
    addToBackStack: Boolean = true,
    enterAnim: Int = R.anim.enter_from_right,
    exitAnim: Int = R.anim.exit_to_right
) = fragmentManager.beginTransaction().apply {
    if (enterAnim != 0 || exitAnim != 0) {
        setCustomAnimations(enterAnim, 0, 0, exitAnim)
    }
    add(containerViewId, fragment, TAG)
}.commitTransaction(addToBackStack, TAG)

fun Fragment.replaceFragment(
    fragment: Fragment,
    TAG: String?,
    containerViewId: Int,
    addToBackStack: Boolean = true,
    enterAnim: Int = 0,
    exitAnim: Int = 0
) = activity?.supportFragmentManager?.beginTransaction()?.apply {
    if (enterAnim != 0 || exitAnim != 0) {
        setCustomAnimations(enterAnim, 0, 0, exitAnim)
    }
    replace(containerViewId, fragment, TAG)
}?.commitTransaction(addToBackStack, TAG)

fun Fragment.addChildFragment(
    childFragment: Fragment,
    TAG: String?,
    containerViewId: Int,
    addToBackStack: Boolean = true,
    enterAnim: Int = 0,
    exitAnim: Int = 0
) = this.childFragmentManager.beginTransaction().apply {
    if (enterAnim != 0 || exitAnim != 0) {
        setCustomAnimations(enterAnim, 0, 0, exitAnim)
    }
    add(containerViewId, childFragment, TAG)
}.commitTransaction(addToBackStack, TAG)

fun Fragment.replaceChildFragment(
    childFragment: Fragment,
    TAG: String?,
    containerViewId: Int,
    addToBackStack: Boolean = true,
    enterAnim: Int = 0,
    exitAnim: Int = 0
) = this.childFragmentManager.beginTransaction().apply {
    if (enterAnim != 0 || exitAnim != 0) {
        setCustomAnimations(enterAnim, 0, 0, exitAnim)
    }
    replace(containerViewId, childFragment, TAG)
}.commitTransaction(addToBackStack, TAG)

@SuppressLint("WrongConstant")
private fun FragmentTransaction.commitTransaction(
    addToBackStack: Boolean = false,
    name: String? = null
) {
    if (addToBackStack) this.addToBackStack(name)
    this.commitAllowingStateLoss()
}

@SuppressLint("WrongConstant")
fun Fragment.showDialogFragment(
    dialogFragment: DialogFragment, TAG: String?,
    addToBackStack: Boolean = false
) {
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    if (addToBackStack) transaction?.addToBackStack(TAG)
    dialogFragment.show(transaction!!, TAG)
}