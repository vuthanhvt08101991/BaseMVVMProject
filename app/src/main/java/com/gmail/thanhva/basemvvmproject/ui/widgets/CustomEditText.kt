package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.listener.OnDrawableClickListener

/**
 *  Create by thanhva on 19/08/2020
 *  Class CustomEditText
 */
@BindingMethods(
    BindingMethod(
        type = CustomEditText::class,
        attribute = "show_icon_clear",
        method = "setShowIconClear"
    )
)
class CustomEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var drawableRight: Drawable? = null
    var drawableRightInt: Int = 0
    var actionX: Int = 0
    var actionY: Int = 0
    var iconClose: Drawable? = null
    var clickListener: OnDrawableClickListener? = null
    var isShowIconRight: Boolean = false

    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == android.R.id.paste) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return super.onTextContextMenuItem(android.R.id.pasteAsPlainText)
            } else {
                onInterceptClipDataToPlainText()
            }
        }
        return super.onTextContextMenuItem(id)
    }

    private fun onInterceptClipDataToPlainText() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip?.let { clip ->
            for (i in 0 until clip.itemCount) {
                val paste: CharSequence? = clip.getItemAt(i).coerceToText(context)
                paste?.run {
                    val clipData = ClipData.newPlainText("rebase_copy", text)
                    val manager = context
                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    manager.setPrimaryClip(clipData)
                }
            }
        }
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.isNotEmpty() && isShowIconRight) {
            setCompoundDrawablesWithIntrinsicBounds(
                context.getDrawable(R.drawable.ic_component_search),
                null,
                iconClose ?: context.getDrawable(R.drawable.ic_clear_input),
                null
            )
        } else {
            setCompoundDrawablesWithIntrinsicBounds(
                context.getDrawable(R.drawable.ic_component_search),
                null,
                null,
                null
            )
        }
    }

    override fun setCompoundDrawables(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        right?.let { drawableRight = it }
        super.setCompoundDrawables(left, top, right, bottom)
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(
        @DrawableRes left: Int, @DrawableRes top: Int,
        @DrawableRes right: Int, @DrawableRes bottom: Int
    ) {
        drawableRightInt = right
        if (right != 0) {
            drawableRight = ResourcesCompat.getDrawable(context.resources, right, null)
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var bounds: Rect?
        if (event.action == MotionEvent.ACTION_DOWN) {
            actionX = event.x.toInt()
            actionY = event.y.toInt()

            drawableRight?.run {
                bounds = this.bounds

                //4: paddingRight in ItemInput view
                val extraTapArea = (4 * resources.displayMetrics.density + 0.5).toInt()

                val x: Int = width - actionX - extraTapArea
                val y: Int = actionY - (height - bounds!!.height()) / 2

                /**If drawble bounds contains the x and y points then move ahead. */
                if (isShowIconRight && bounds!!.contains(x, y)) {
                    clickListener?.let {
                        if (!it.onClick(
                                this@CustomEditText,
                                OnDrawableClickListener.DrawablePosition.RIGHT
                            )
                        ) {
                            this@CustomEditText.setText("")
                        }
                        return false
                    }
                    this@CustomEditText.setText("")
                    return false
                }
                return super.onTouchEvent(event)
            }
        }
        return super.onTouchEvent(event)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        drawableRight = null
    }

    fun setShowIconClear(isShow: Boolean) {
        this.isShowIconRight = isShow
    }
}