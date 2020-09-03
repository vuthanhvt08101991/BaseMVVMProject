package com.gmail.thanhva.basemvvmproject.ui.screen.first_screen

import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.databinding.FragmentFirstBinding
import com.gmail.thanhva.basemvvmproject.ui.base.BaseFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  Create by thanhva on 28/08/2020
 *  Class FirstFragment
 */
class FirstFragment : BaseFragmentBinding<FragmentFirstBinding, FirstViewModel>() {

    override val viewModel: FirstViewModel by viewModel()

    override val layoutId = R.layout.fragment_first

    override fun getTagFragment() = FirstFragment::class.java.simpleName

    override fun initView() {

    }

    override fun initData() {

    }

    override fun observeField() {

    }

    companion object {
        fun newInstance() = FirstFragment()
    }
}