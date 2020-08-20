package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.content.Context
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.util.AttributeSet
import androidx.core.text.clearSpans
import com.gmail.thanhva.basemvvmproject.R

/**
 *  Create by thanhva on 19/08/2020
 *  Class CustomTitleTextView
 */
class CustomTitleTextView : CustomTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs, defStyleAttr)
    }

    private fun initView(attrs: AttributeSet?, defStyleAttr: Int) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleTextView, defStyleAttr, 0)
        val urlIconTitle = typeArray.getString(R.styleable.CustomTitleTextView_url)
        if (!urlIconTitle.isNullOrEmpty()) {
            marginStartForFirstLine()
        }
        typeArray.recycle()
    }

    fun setUrl(url: String?) {
        if (url.isNullOrEmpty()) {
            return
        }
        marginStartForFirstLine()
    }

    private fun marginStartForFirstLine() {
        val sb = SpannableString(this.text)
        sb.clearSpans()
        if (this.text.isNotEmpty()) {
            sb.setSpan(LeadingMarginSpan.Standard(context.resources.getDimension(R.dimen.dp_20).toInt(), 0), 0, 0, 0)
        }
        this.text = sb
    }
}