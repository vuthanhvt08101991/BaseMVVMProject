package com.gmail.thanhva.basemvvmproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.thanhva.basemvvmproject.BuildConfig
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.data.local.PrefHelper
import com.gmail.thanhva.basemvvmproject.data.model.ItemShare
import com.gmail.thanhva.basemvvmproject.ui.widgets.DividerItemDecoration
import org.koin.java.KoinJavaComponent

/**
 *  Create by thanhva on 18/08/2020
 *  Class BaseUtils
 */
fun AppCompatActivity.hideKeyboard() {
    val imm: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun AppCompatActivity.showKeyboard() {
    val imm: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.showSoftInput(view, 0)
}

fun View.hideKeyboardFromView() {
    val imm = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
    this.clearFocus()
}

fun AppCompatActivity.setStatusBar(color: Int) {
    this.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = ContextCompat.getColor(context, color)
    }
}

fun RecyclerView.setUpRecyclerViewVertical() {
    this.setUpRecyclerViewVertical(false)
}

fun RecyclerView.setUpRecyclerViewHorizontal() {
    this.setUpRecyclerViewHorizontal(false)
}

fun RecyclerView.setUpRecyclerViewVertical(isDecoration: Boolean, isNestedScroll: Boolean = false) {
    val linearLayoutManager =
        LinearLayoutManager(this.context, RecyclerView.VERTICAL, false).apply {
            isSmoothScrollbarEnabled = true
        }
    this.apply {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        isNestedScrollingEnabled = isNestedScroll
        if (isDecoration) {
            addItemDecoration(DividerItemDecoration(this.context, R.drawable.divider))
        }
    }
}

fun RecyclerView.setUpRecyclerViewHorizontal(isDecoration: Boolean, isNestedScroll: Boolean = false) {
    val linearLayoutManager =
        LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false).apply {
            isSmoothScrollbarEnabled = true
        }
    this.apply {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        isNestedScrollingEnabled = isNestedScroll
        if (isDecoration) {
            addItemDecoration(DividerItemDecoration(this.context, R.drawable.divider))
        }
    }
}

fun RecyclerView.setUpRecyclerViewGrid(isDecoration: Boolean, spanCount: Int) {
    val grid = GridLayoutManager(this.context, spanCount).apply {
        isSmoothScrollbarEnabled = true
        isAutoMeasureEnabled = true
    }
    this.apply {
        setHasFixedSize(true)
        isNestedScrollingEnabled = false
        layoutManager = grid
        if (isDecoration) {
            addItemDecoration(DividerItemDecoration(this.context, R.drawable.divider))
        }
    }
}

fun getBaseUrl(): String {
    val appPrefs: PrefHelper by KoinJavaComponent.inject(PrefHelper::class.java)
    val url = appPrefs.getBaseUrl()
    if (url.endsWith("/")) return url
    return "$url/"
}


@Nullable
fun getTopFragment(fragmentManager: FragmentManager): Fragment? {
    val count = fragmentManager.backStackEntryCount
    if (count == 0) return null
    val backEntry = fragmentManager.getBackStackEntryAt(count - 1)
    return fragmentManager.findFragmentByTag(backEntry.name)
}

fun getWidthScreen(activity: Activity?): Int {
    val displayMetrics = DisplayMetrics()
    activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

@SuppressLint("DefaultLocale")
fun getListApplicationShare(context: Context?, url: String?): ArrayList<ItemShare> {
    val resultList = ArrayList<ItemShare>()
    if (url.isNullOrEmpty()) return resultList

    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
    val resInfoList = context?.packageManager?.queryIntentActivities(sharingIntent, 0)

    resInfoList?.run {
        for (resInfo in resInfoList) {
            val packageName = resInfo.activityInfo.packageName
            if (!packageName.toLowerCase().contains(BuildConfig.APPLICATION_ID) &&
                !resInfo.activityInfo.name.toLowerCase().contains("SendTextToClipboardActivity".toLowerCase())
            ) {
                val intent = Intent().apply {
                    component = ComponentName(packageName, resInfo.activityInfo.name)
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    `package` = packageName
                    putExtra(Intent.EXTRA_TEXT, url)
                }
                context.packageManager?.let { packageManager ->
                    val icon = packageManager.getApplicationIcon(packageName)
                    val applicationName = packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(
                            packageName,
                            PackageManager.GET_META_DATA
                        )
                    ).toString()
                    resultList.add(ItemShare(resInfo, packageName, applicationName, icon))
                }
            }
        }
    }
    return resultList
}

fun setAnimationItemClick(v: View?, event: MotionEvent?) {
    if (event?.action == MotionEvent.ACTION_DOWN) {
        v?.scaleX = 0.98f
        v?.scaleY = 0.98f
    } else if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
        v?.scaleX = 1f
        v?.scaleY = 1f
    }
}

fun convertDpToPx(context: Context, dp: Float): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    context.resources.displayMetrics
).toInt()