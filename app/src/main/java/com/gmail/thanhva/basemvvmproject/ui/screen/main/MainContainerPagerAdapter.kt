package com.gmail.thanhva.basemvvmproject.ui.screen.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 *  Create by thanhva on 28/08/2020
 *  Class MainContainerPagerAdapter
 */
class MainContainerPagerAdapter(
    fm: FragmentManager,
    private val listFragment: List<Fragment>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = listFragment[position]

    override fun getCount(): Int = MainContainerFragment.NUMBER_PAGE_LOAD
}
