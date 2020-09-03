package com.gmail.thanhva.basemvvmproject.data.constant

import androidx.annotation.IntDef
import com.gmail.thanhva.basemvvmproject.data.constant.NavBottomType.Companion.FIRST
import com.gmail.thanhva.basemvvmproject.data.constant.NavBottomType.Companion.SECOND

/**
 *  Create by thanhva on 28/08/2020
 *  Class NavBottomType
 */
@IntDef(FIRST, SECOND)
annotation class NavBottomType {
    companion object {
        const val FIRST = 0
        const val SECOND = 1
    }
}