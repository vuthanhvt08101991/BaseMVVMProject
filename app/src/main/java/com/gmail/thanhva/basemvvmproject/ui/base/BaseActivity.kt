package com.gmail.thanhva.basemvvmproject.ui.base

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gmail.thanhva.basemvvmproject.data.constant.ThresholdClickTime
import com.gmail.thanhva.basemvvmproject.ui.widgets.LoadingDialog
import com.gmail.thanhva.basemvvmproject.utils.DialogUtils

/**
 *  Create by thanhva on 18/08/2020
 *  Class BaseActivity
 */
abstract class BaseActivity<ViewModel : BaseViewModel> : AppCompatActivity() {

    private var messageDialog: AlertDialog? = null
    private var loadingDialog: LoadingDialog? = null
    var thresholdClickTime: ThresholdClickTime? = null
    abstract val viewModel: ViewModel

    open fun beforeAddContent() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeAddContent()
        thresholdClickTime = ThresholdClickTime()

    }

    fun showLoading(cancelable: Boolean) {
        if ((loadingDialog != null && loadingDialog!!.isShowing) || this.isFinishing) return
        loadingDialog = LoadingDialog(context = this).apply {
            setCancelable(cancelable)
            setOwnerActivity(this@BaseActivity)
        }
        if (!this.isFinishing) {
            loadingDialog?.show()
        }
    }

    fun showLoading() {
        showLoading(false)
    }

    fun hideLoading() {
        if (loadingDialog != null && loadingDialog?.isShowing == true && !this.isFinishing) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    fun showErrorDialog(msg: Int, textPositive: Int) {
        if (messageDialog != null || messageDialog?.isShowing == true || this.isFinishing) return
        messageDialog = DialogUtils.showMessage(
            context = this,
            message = getString(msg),
            textPositive = getString(textPositive),
            positiveListener = DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                messageDialog = null
            }
        )
    }

    fun showErrorDialog(msg: String, textPositive: String) {
        if (messageDialog != null || messageDialog?.isShowing == true || this.isFinishing) return
        messageDialog = DialogUtils.showMessage(
            context = this,
            message = msg,
            textPositive = textPositive,
            positiveListener = DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                messageDialog = null
            }
        )
    }

    override fun onStop() {
        loadingDialog?.dismiss()
        messageDialog?.dismiss()
        super.onStop()
    }
}