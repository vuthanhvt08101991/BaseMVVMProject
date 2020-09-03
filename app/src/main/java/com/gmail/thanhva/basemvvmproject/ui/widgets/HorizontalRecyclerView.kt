package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  Create by thanhva on 31/08/2020
 *  Class HorizontalRecyclerView
 */
class HorizontalRecyclerView : RecyclerView {
    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        val linearLayoutManager =
            LinearLayoutManager(this.context, HORIZONTAL, false).apply {
                isSmoothScrollbarEnabled = true
            }
        this.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = linearLayoutManager
        }
    }
}