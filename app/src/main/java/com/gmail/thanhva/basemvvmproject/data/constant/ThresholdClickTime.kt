package com.gmail.thanhva.basemvvmproject.data.constant

import android.os.Handler

/**
 *  Create by thanhva on 18/08/2020
 *  Class ThresholdClickTime
 */
class ThresholdClickTime {
    private var isBlockClick = false

    fun isBlockClick(): Boolean {
        return isBlockClick
    }

    fun setBlockClick(blockClick: Boolean) {
        isBlockClick = blockClick
        if (blockClick) {
            Handler().postDelayed({ isBlockClick = false }, Constants.THRESHOLD_CLICK_TIME.toLong())
        }
    }
}