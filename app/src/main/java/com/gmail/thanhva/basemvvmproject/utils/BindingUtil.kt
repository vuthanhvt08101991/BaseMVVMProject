package com.gmail.thanhva.basemvvmproject.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.gmail.thanhva.basemvvmproject.BuildConfig
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.data.constant.Constants
import java.io.File
import java.nio.charset.StandardCharsets

/**
 *  Create by thanhva on 19/08/2020
 *  Class BindingUtil
 */
val BASE_AUTHEN_GLIDE = "Basic " + Base64.encodeToString(
    (BuildConfig.USER_NAME_AUTHEN + ":" + BuildConfig.PASSWORD_AUTHEN).toByteArray(
        StandardCharsets.UTF_8
    ), Base64.DEFAULT
)

@SuppressLint("CheckResult")
@BindingAdapter(
    value = ["loadImage", "placeholder", "centerCrop", "fitCenter", "circleCrop", "cacheSource", "animation", "roundedCorner"],
    requireAll = false
)
fun ImageView.loadImage(
    url: String?, placeHolder: Drawable?,
    centerCrop: Boolean = false,
    fitCenter: Boolean = false,
    circleCrop: Boolean = false,
    isCacheSource: Boolean = false,
    animation: Boolean = false,
    roundedCorner: Boolean = false
) {
    if (url.isNullOrEmpty()) {
        setImageDrawable(placeHolder)
        return
    }

    val requestBuilder: RequestBuilder<Drawable> = Glide.with(context).load(createGlideURL(url))

    val requestOptions = RequestOptions().diskCacheStrategy(
        if (isCacheSource) DiskCacheStrategy.DATA else DiskCacheStrategy.RESOURCE
    ).placeholder(placeHolder)

    if (!animation) requestOptions.dontAnimate()
    if (centerCrop) requestOptions.centerCrop()
    if (fitCenter) requestOptions.fitCenter()
    if (circleCrop) requestOptions.circleCrop()
    if (roundedCorner) requestOptions.transforms(
        CenterCrop(),
        RoundedCorners(this.resources.getDimension(R.dimen.dp_10).toInt())
    )
    val file = File(url)
    if (file.exists()) {
        requestOptions.signature(ObjectKey(file.lastModified().toString()))
    }
    requestBuilder.apply(requestOptions).into(this)
}

@BindingAdapter("clickSafe")
fun View.setClickSafe(listener: View.OnClickListener?) {
    setOnClickListener(object : View.OnClickListener {
        var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < Constants.THRESHOLD_CLICK_TIME) {
                return
            }
            listener?.onClick(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

@BindingAdapter("setMaxLine")
fun TextView.setMaxLine(maxLine: Int?) {
    maxLine?.let {
        if (it > 0) {
            maxLines = it
            ellipsize = TextUtils.TruncateAt.END
        }
    }
}

fun createGlideURL(url: String): GlideUrl {
    val header = LazyHeaders.Builder()
        .addHeader(
            "Authorization", BASE_AUTHEN_GLIDE
        ).build()
    return GlideUrl(url, header)
}

@BindingAdapter("visiable")
fun View.visiable(boolean: Boolean) {
    this.visibility = if (boolean) View.VISIBLE else View.GONE
}