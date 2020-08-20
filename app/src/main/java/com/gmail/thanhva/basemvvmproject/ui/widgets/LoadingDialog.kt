package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.gmail.thanhva.basemvvmproject.R

/**
 *  Create by thanhva on 18/08/2020
 *  Class LoadingDialog
 */
class LoadingDialog(context: Context) : AlertDialog(context) {
    private var rootView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        rootView = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
        setView(rootView)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0f)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        this.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                rootView?.clearAnimation()
                dismiss()
                ownerActivity?.onBackPressed()
            }
            true
        }
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_loading_show)
        rootView?.visibility = View.VISIBLE
        rootView?.startAnimation(animation)
    }
}