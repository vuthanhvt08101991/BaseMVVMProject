package com.gmail.thanhva.basemvvmproject.ui.base

import android.view.MotionEvent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gmail.thanhva.basemvvmproject.BR
import com.gmail.thanhva.basemvvmproject.utils.setAnimationItemClick

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseViewHolder
 */
abstract class BaseViewHolder<Item>(var binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnTouchListener {

    var itemDisplay: Item? = null

    fun onBindData(itemDisplay: Item) {
        // TODO: remove comments
        // binding.setVariable(BR.viewModel, this)
        this.itemDisplay = itemDisplay
        bindData(itemDisplay)
    }

    abstract fun bindData(itemDisplay: Item)

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        setAnimationItemClick(v, event)
        return false
    }
}