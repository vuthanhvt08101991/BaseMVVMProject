package com.gmail.thanhva.basemvvmproject.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.gmail.thanhva.basemvvmproject.R

/**
 *  Create by thanhva on 18/08/2020
 *  Class DialogUtils
 */
object DialogUtils {
    fun createLoadingDialog(
        context: Context?, cancelable: Boolean = false,
        canceledOnTouchOutside: Boolean = false
    ): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context)
            .setView(R.layout.loading_dialog)
            .create().apply {
                setCancelable(cancelable)
                setCanceledOnTouchOutside(canceledOnTouchOutside)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
    }

    fun showLoadingDialog(
        context: Context?, cancelable: Boolean = false,
        canceledOnTouchOutside: Boolean = false
    ): AlertDialog? {
        if (context == null) return null
        val dialog: AlertDialog? = createLoadingDialog(context, cancelable)
        dialog?.apply {
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            show()
        }
        return dialog
    }

    fun showMessage(
        context: Context?, title: String? = null, message: String? = null,
        textPositive: String? = null, positiveListener: DialogInterface.OnClickListener? = null,
        textNegative: String? = null, negativeListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false
    ): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            if (textPositive != null) {
                setPositiveButton(textPositive, positiveListener)
            }
            if (textNegative != null) {
                setPositiveButton(textNegative, negativeListener)
            }
            setCancelable(cancelable)

        }.create().apply {
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            show()
        }
    }

    fun showMessage(
        context: Context?, title: Int? = null, message: Int? = null,
        textPositive: Int? = null, positiveListener: DialogInterface.OnClickListener? = null,
        textNegative: Int? = null, negativeListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false
    ): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context).apply {
            if (title != null) setTitle(title)
            if (message != null) setMessage(message)
            if (textPositive != null) {
                setPositiveButton(textPositive, positiveListener)
            }
            if (textNegative != null) {
                setPositiveButton(textNegative, negativeListener)
            }
            setCancelable(cancelable)
        }.create().apply {
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            show()
        }
    }
}