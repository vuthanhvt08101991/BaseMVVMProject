package com.gmail.thanhva.basemvvmproject.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.gmail.thanhva.basemvvmproject.BR

/**
 *  Create by thanhva on 18/08/2020
 *  Class BaseDialogFragment
 */
abstract class BaseDialogFragment<ViewBinding : ViewDataBinding, ViewModel : androidx.lifecycle.ViewModel> :
    DialogFragment() {

    lateinit var viewBinding: ViewBinding

    abstract val layoutId: Int
    abstract val viewModel: ViewModel

    abstract fun observeField()
    abstract fun initView()
    abstract fun initData()

    open fun beforeAddContent() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeAddContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        performDataBinding(inflater, container)
        initView()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeField()
        initData()
    }

    private fun performDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            root.isClickable = true
            // TODO: remove comments
            // setVariable(BR.viewModel, viewModel)
            executePendingBindings()
        }

    }
}