package com.gmail.thanhva.basemvvmproject.utils

import android.content.Context
import android.graphics.Typeface

/**
 *  Create by thanhva on 19/08/2020
 *  Class FontsUtils
 */
object FontsUtils {
    val FONT_UBUNTU_LIGHT = 0
    val FONT_YESTERYEAR = 1

    private val NUMBER_CUSTOM_FONTS = 2

    private var fontsLoaded = false

    private val fonts = arrayOfNulls<Typeface>(NUMBER_CUSTOM_FONTS)

    private val fontPath =
        arrayOf(
            "fonts/ubuntu_light.xml",
            "fonts/yesteryear.xml"
        )

    fun getTypeface(context: Context, fontIdentifier: Int): Typeface {
        if (!fontsLoaded) {
            loadFonts(context)
        }
        return fonts[fontIdentifier]!!
    }


    private fun loadFonts(context: Context) {
        for (i in 0 until NUMBER_CUSTOM_FONTS) {
            fonts[i] = Typeface.createFromAsset(context.assets, fontPath[i])
        }
        this.fontsLoaded = true
    }
}