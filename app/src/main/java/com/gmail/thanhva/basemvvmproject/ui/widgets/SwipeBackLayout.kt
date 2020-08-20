package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.ScrollView
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.gmail.thanhva.basemvvmproject.ui.widgets.DragEdge.Companion.BOTTOM
import com.gmail.thanhva.basemvvmproject.ui.widgets.DragEdge.Companion.LEFT
import com.gmail.thanhva.basemvvmproject.ui.widgets.DragEdge.Companion.RIGHT
import com.gmail.thanhva.basemvvmproject.ui.widgets.DragEdge.Companion.TOP
import kotlin.math.abs

/**
 *  Create by thanhva on 18/08/2020
 *  Class SwipeBackLayout
 */
@Suppress("DEPRECATION")
class SwipeBackLayout : ViewGroup {

    companion object {
        const val AUTO_FINISHED_SPEED_LIMIT = 2000.0
        const val BACK_FACTOR = 0.3f
    }

    var coordinatesCanSwipe = 300
    var dragEdge = TOP
    private var viewDragHelper: ViewDragHelper? = null
    private var target: View? = null
    private var scrollChild: View? = null
    private var verticalDragRange = 0
    private var horizontalDragRange = 0
    private var draggingState = 0
    private var draggingOffset = 0
    /**
     * Whether allow to pull this layout.
     */
    private var enablePullToBack = true

    /**
     * the anchor of calling finish.
     */
    var finishAnchor = 0f

    var enableFlingBack = true

    var enableSwipe = true

    var swipeBackListener: SwipeBackListener? = null

    constructor(context: Context) : super(context, null) {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, ViewDragHelperCallBack())
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, ViewDragHelperCallBack())
    }

    fun canChildScrollUp() = ViewCompat.canScrollVertically(scrollChild, -1)

    fun canChildScrollDown() = ViewCompat.canScrollVertically(scrollChild, 1)

    private fun canChildScrollRight() = ViewCompat.canScrollHorizontally(scrollChild, -1)

    private fun canChildScrollLeft() = ViewCompat.canScrollHorizontally(scrollChild, 1)

    private fun getDragRange(): Int {
        return when (dragEdge) {
            TOP, BOTTOM -> verticalDragRange
            LEFT, RIGHT -> horizontalDragRange
            else -> verticalDragRange
        }
    }

    private fun ensureTarget() {
        if (target == null) {
            check(childCount <= 1) { "SwipeBackLayout must contains only one direct child" }
            target = getChildAt(0)
            if (scrollChild == null && target != null) {
                if (target is ViewGroup) {
                    target?.let { findScrollView(it as ViewGroup) }
                } else {
                    scrollChild = target
                }
            }
        }
    }

    private fun findScrollView(viewGroup: ViewGroup) {
        scrollChild = viewGroup
        if (viewGroup.childCount > 0) {
            val count = viewGroup.childCount
            var child: View?
            for (i in 0 until count) {
                child = viewGroup.getChildAt(i)
                if (child is AbsListView
                    || child is ScrollView
                    || child is ViewPager
                    || child is WebView
                ) {
                    scrollChild = child
                    return
                }
            }
        }
    }

    private fun finish() {
        swipeBackListener?.onSwipeComplete()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = measuredWidth
        val height = measuredHeight
        if (childCount == 0) return

        val child = getChildAt(0)

        val childWidth = width - paddingLeft - paddingRight
        val childHeight = height - paddingTop - paddingBottom
        val childLeft = paddingLeft
        val childTop = paddingTop
        val childRight = childLeft + childWidth
        val childBottom = childTop + childHeight
        child.layout(childLeft, childTop, childRight, childBottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        check(childCount <= 1) { "SwipeBackLayout must contains only one direct child." }

        if (childCount > 0) {
            val measureWidth = MeasureSpec.makeMeasureSpec(
                measuredWidth - paddingLeft - paddingRight, MeasureSpec.EXACTLY
            )
            val measureHeight = MeasureSpec.makeMeasureSpec(
                measuredHeight - paddingTop - paddingBottom,
                MeasureSpec.EXACTLY
            )
            getChildAt(0).measure(measureWidth, measureHeight)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        verticalDragRange = h
        horizontalDragRange = w

        when (dragEdge) {
            TOP, BOTTOM -> finishAnchor =
                if (finishAnchor > 0) finishAnchor else verticalDragRange * BACK_FACTOR
            LEFT, RIGHT -> finishAnchor =
                if (finishAnchor > 0) finishAnchor else horizontalDragRange * BACK_FACTOR
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var handled = false
        ensureTarget()
        if (isEnabled && ev!!.x < coordinatesCanSwipe && enableSwipe) {
            handled = viewDragHelper?.shouldInterceptTouchEvent(ev) ?: false
        } else {
            viewDragHelper?.cancel()
        }
        return handled || super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { viewDragHelper?.processTouchEvent(it) }
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper?.continueSettling(true) == true) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun backBySpeed(xvel: Float, yvel: Float): Boolean {
        when (dragEdge) {
            TOP, BOTTOM -> if (abs(
                    yvel
                ) > abs(xvel) && abs(yvel) > AUTO_FINISHED_SPEED_LIMIT
            ) {
                return if (dragEdge == TOP) !canChildScrollUp() else !canChildScrollDown()
            }
            LEFT, RIGHT -> if (abs(
                    xvel
                ) > abs(yvel) && abs(xvel) > AUTO_FINISHED_SPEED_LIMIT
            ) {
                return if (dragEdge == LEFT) !canChildScrollLeft() else !canChildScrollRight()
            }
        }
        return false
    }

    private fun smoothScrollToX(finalLeft: Int) {
        if (viewDragHelper!!.settleCapturedViewAt(finalLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this@SwipeBackLayout)
        }
    }

    private fun smoothScrollToY(finalTop: Int) {
        if (viewDragHelper!!.settleCapturedViewAt(0, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this@SwipeBackLayout)
        }
    }


    inner class ViewDragHelperCallBack : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == target && enablePullToBack
        }

        override fun getViewVerticalDragRange(child: View) = verticalDragRange

        override fun getViewHorizontalDragRange(child: View) = horizontalDragRange

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            var result = 0
            if (dragEdge == TOP && !canChildScrollUp() && top > 0) {
                val topBound: Int = paddingTop
                val bottomBound: Int = verticalDragRange
                result = top.coerceAtLeast(topBound).coerceAtMost(bottomBound)
            } else if (dragEdge == BOTTOM && !canChildScrollDown()
                && top < 0
            ) {
                val topBound: Int = -verticalDragRange
                val bottomBound: Int = paddingTop
                result = top.coerceAtLeast(topBound).coerceAtMost(bottomBound)
            }
            return result
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var result = 0
            if (dragEdge == LEFT && !canChildScrollRight() && left > 0) {
                val leftBound: Int = paddingLeft
                val rightBound: Int = horizontalDragRange
                result = left.coerceAtLeast(leftBound).coerceAtMost(rightBound)
            } else if (dragEdge == RIGHT && !canChildScrollLeft()
                && left < 0
            ) {
                val leftBound: Int = -horizontalDragRange
                val rightBound: Int = paddingLeft
                result = left.coerceAtLeast(leftBound).coerceAtMost(rightBound)
            }
            return result
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == draggingState) return
            if ((draggingState == ViewDragHelper.STATE_DRAGGING
                        || draggingState == ViewDragHelper.STATE_SETTLING)
                && state == ViewDragHelper.STATE_IDLE
            ) { // the view stopped from moving.
                if (draggingOffset == getDragRange()) {
                    scrollChild?.visibility = View.GONE
                    target?.visibility = View.GONE
                    finish()
                } else {
                    swipeBackListener?.onSwipeCancel()
                }
            }
            draggingState = state
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            when (dragEdge) {
                TOP, BOTTOM -> draggingOffset =
                    abs(top)
                LEFT, RIGHT -> draggingOffset =
                    abs(left)
                else -> {
                }
            }
            //The proportion of the sliding.
            var fractionAnchor = if (finishAnchor == 0f) {
                0f
            } else {
                draggingOffset / finishAnchor
            }
            if (fractionAnchor >= 1) fractionAnchor = 1f
            getDragRange()
            var fractionScreen = if (getDragRange() == 0) {
                0f
            } else {
                draggingOffset.toFloat() / getDragRange()
            }
            if (fractionScreen >= 1) fractionScreen = 1f
            swipeBackListener?.onViewPositionChanged(fractionAnchor, fractionScreen)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (draggingOffset == 0) return
            if (draggingOffset == getDragRange()) return
            var isBack = false
            if (enableFlingBack && backBySpeed(xvel, yvel)) {
                isBack = !canChildScrollUp()
            } else if (draggingOffset >= finishAnchor) {
                isBack = true
            } else if (draggingOffset < finishAnchor) {
                isBack = false
            }
            val finalLeft: Int
            val finalTop: Int
            when (dragEdge) {
                LEFT -> {
                    finalLeft = if (isBack) horizontalDragRange else 0
                    smoothScrollToX(finalLeft)
                }
                RIGHT -> {
                    finalLeft = if (isBack) -horizontalDragRange else 0
                    smoothScrollToX(finalLeft)
                }
                TOP -> {
                    finalTop = if (isBack) verticalDragRange else 0
                    smoothScrollToY(finalTop)
                }
                BOTTOM -> {
                    finalTop = if (isBack) -verticalDragRange else 0
                    smoothScrollToY(finalTop)
                }
            }
        }
    }


    interface SwipeBackListener {
        /**
         * Return scrolled fraction of the layout.
         *
         * @param fractionAnchor relative to the anchor.
         * @param fractionScreen relative to the screen.
         */
        fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float)

        fun onSwipeComplete()

        fun onSwipeCancel()
    }

}

@IntDef(LEFT, RIGHT, TOP, BOTTOM)
annotation class DragEdge {
    companion object {
        const val LEFT = 0
        const val RIGHT = 1
        const val TOP = 2
        const val BOTTOM = 3
    }
}