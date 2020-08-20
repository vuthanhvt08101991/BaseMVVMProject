package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.gmail.thanhva.basemvvmproject.data.model.PositionSpecialText
import com.gmail.thanhva.basemvvmproject.utils.FontsUtils
import java.util.regex.Pattern

/**
 *  Create by thanhva on 19/08/2020
 *  Class CustomTextView
 */
open class CustomTextView : AppCompatTextView {

    private var defaultStyle = Typeface.NORMAL

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setTypeface(tf: Typeface?, style: Int) {
        defaultStyle = style
        super.setTypeface(tf, style)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (text.isNullOrEmpty()) {
            super.setText(text, type)
        } else {
            val positionTexts = mutableListOf<PositionSpecialText>()

            val matcher = Pattern.compile(SPENCAIL_TEXT).matcher(text)

            val newText = SpannableStringBuilder(text)

            while (matcher.find()) {
                positionTexts.add(
                    PositionSpecialText(
                        matcher.start(),
                        matcher.end()
                    )
                )
            }

            if (defaultStyle == Typeface.NORMAL) {
                super.setText(
                    setSpecialText(positionTexts, newText, FontsUtils.FONT_UBUNTU_LIGHT),
                    type
                )
            } else {
                super.setText(setSpecialText(positionTexts, newText, FontsUtils.FONT_YESTERYEAR), type)
            }
        }
    }

    private fun setSpecialText(
        positionText: MutableList<PositionSpecialText>,
        newText: SpannableStringBuilder,
        font: Int
    ): SpannableStringBuilder {
        for (i in positionText) {
            newText.setSpan(
                CustomTypefaceSpan(
                    FontsUtils.getTypeface(
                        context,
                        font
                    )
                )
                , i.start, i.end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
        return newText
    }

    companion object {
        const val SPENCAIL_TEXT = "[、。！？〈〉《》「」『』【】〔〕・（）：；［］｛｝]"
    }
}