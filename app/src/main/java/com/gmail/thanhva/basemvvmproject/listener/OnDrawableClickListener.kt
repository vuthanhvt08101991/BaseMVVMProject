package com.gmail.thanhva.basemvvmproject.listener

import android.view.View
import androidx.annotation.IntDef

interface OnDrawableClickListener {
    /**
     *  @return True if the event was handled, false otherwise.
     */
    fun onClick(view: View, @DrawablePosition target: Int): Boolean

    @IntDef(
        DrawablePosition.TOP,
        DrawablePosition.BOTTOM,
        DrawablePosition.LEFT,
        DrawablePosition.RIGHT
    )

    annotation class DrawablePosition {
        companion object {
            const val TOP = 1
            const val BOTTOM = 2
            const val LEFT = 3
            const val RIGHT = 4
        }
    }
}
