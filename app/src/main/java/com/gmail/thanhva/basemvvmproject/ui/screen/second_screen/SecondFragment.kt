package com.gmail.thanhva.basemvvmproject.ui.screen.second_screen

import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.databinding.FragmentSecondBinding
import com.gmail.thanhva.basemvvmproject.ui.base.BaseFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  Create by thanhva on 28/08/2020
 *  Class SecondFragment
 */
class SecondFragment : BaseFragmentBinding<FragmentSecondBinding, SecondViewModel>() {

    override val viewModel: SecondViewModel by viewModel()

    override val layoutId = R.layout.fragment_second

    override fun getTagFragment() = SecondFragment::class.java.simpleName

    override fun initView() {

    }

    override fun initData() {

    }

    override fun observeField() {

    }

    companion object {
        fun newInstance() = SecondFragment()
    }
}