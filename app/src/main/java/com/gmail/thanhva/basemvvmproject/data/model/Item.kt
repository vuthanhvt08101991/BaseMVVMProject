package com.gmail.thanhva.basemvvmproject.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 *  Create by thanhva on 18/08/2020
 *  Class Item
 */
@Parcelize
data class Item(
    @Expose
    @SerializedName("attr1")
    val attr1: String? = null,

    @Expose
    @SerializedName("attr2")
    val attr2: String? = null
) : Parcelable