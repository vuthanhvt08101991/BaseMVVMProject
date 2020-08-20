package com.gmail.thanhva.basemvvmproject.data.model

import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

/**
 *  Create by thanhva on 18/08/2020
 *  Class ItemShare
 */
data class ItemShare(
    var resInfo: ResolveInfo? = null,
    var pakage: String? = null,
    var applicationName: String? = null,
    var icon: Drawable? = null
)