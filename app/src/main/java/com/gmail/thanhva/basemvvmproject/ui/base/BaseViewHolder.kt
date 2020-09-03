package com.gmail.thanhva.basemvvmproject.ui.base

import android.view.MotionEvent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gmail.thanhva.basemvvmproject.BR
import com.gmail.thanhva.basemvvmproject.listener.OnItemClickRecyclerView
import com.gmail.thanhva.basemvvmproject.utils.setAnimationItemClick

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseViewHolder
 */
abstract class BaseViewHolder<Item>(
    var binding: ViewDataBinding,
    val listener: OnItemClickRecyclerView<Item>?
) : RecyclerView.ViewHolder(binding.root), View.OnTouchListener {

    var item: Item? = null

    fun onBindData(item: Item) {
         binding.setVariable(BR.viewModel, this)
        this.item = item
        bindData(item)
    }

    abstract fun bindData(item: Item)

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        setAnimationItemClick(v, event)
        return false
    }
}