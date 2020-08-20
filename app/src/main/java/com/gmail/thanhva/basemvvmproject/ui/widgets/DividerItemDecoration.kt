package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 *  Create by thanhva on 18/08/2020
 *  Class DividerItemDecoration
 */
class DividerItemDecoration : RecyclerView.ItemDecoration {

    private val ATTRS = intArrayOf(R.attr.listDivider)

    private var divider: Drawable? = null

    /**
     * Default divider will be used
     */
    constructor(context: Context) {
        val styledAttributes = context.obtainStyledAttributes(ATTRS)
        divider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()
    }

    /**
     * Custom divider will be used
     */
    constructor(context: Context?, resId: Int) {
        divider = ContextCompat.getDrawable(context!!, resId)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            divider?.apply {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + this.intrinsicHeight
                setBounds(left, top, right, bottom)
                draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            left = 0
            right = 0
            top = 0
            bottom = divider?.intrinsicHeight ?: 0
        }
    }
}