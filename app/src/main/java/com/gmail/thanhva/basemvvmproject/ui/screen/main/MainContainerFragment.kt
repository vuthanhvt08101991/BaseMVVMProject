package com.gmail.thanhva.basemvvmproject.ui.screen.main

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.data.constant.NavBottomType
import com.gmail.thanhva.basemvvmproject.databinding.FragmentMainContainerBinding
import com.gmail.thanhva.basemvvmproject.listener.OnBackListener
import com.gmail.thanhva.basemvvmproject.ui.base.BaseFragmentBinding
import com.gmail.thanhva.basemvvmproject.ui.screen.first_screen.wrapper.FirstWrapperFragment
import com.gmail.thanhva.basemvvmproject.ui.screen.second_screen.wrapper.SecondWrapperFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  Create by thanhva on 28/08/2020
 *  Class MainContainerFragment
 */
class MainContainerFragment : BaseFragmentBinding<FragmentMainContainerBinding, MainContainerViewModel>(),
    OnBackListener, BottomNavigationView.OnNavigationItemSelectedListener {

    override val viewModel: MainContainerViewModel by viewModel()

    override val layoutId = R.layout.fragment_main_container

    private var firstWrapperFragment = FirstWrapperFragment.newInstance()

    private var secondWrapperFragment = SecondWrapperFragment.newInstance()

    private val listOnBackListener = ArrayList<OnBackListener>()

    override fun getTagFragment() = MainContainerFragment::class.java.simpleName

    override fun initView() {

    }

    override fun initData() {
        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        val listFragment = ArrayList<Fragment>().apply {
            add(firstWrapperFragment)
            add(secondWrapperFragment)
        }

        listOnBackListener.apply {
            add(firstWrapperFragment)
            add(secondWrapperFragment)
        }

        if (this.isAdded) {
            viewBinding.viewPagerMain.apply {
                adapter = MainContainerPagerAdapter(childFragmentManager, listFragment)
                setScrollDurationFactor(0.0)
                offscreenPageLimit = NUMBER_PAGE_LOAD
            }
        }
    }

    override fun observeField() {

    }

    override fun onBackListener() {
        when (viewBinding.viewPagerMain.currentItem) {
            NavBottomType.FIRST -> {
                firstWrapperFragment.onBackListener()
            }

            NavBottomType.SECOND -> {
                secondWrapperFragment.onBackListener()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigationFirst -> {
                viewBinding.viewPagerMain.currentItem = NavBottomType.FIRST
            }

            R.id.navigationSecond -> {
                viewBinding.viewPagerMain.currentItem = NavBottomType.SECOND
            }
        }
        return true
    }

    companion object {
        fun newInstance() = MainContainerFragment()
        const val NUMBER_PAGE_LOAD = 2
    }
}