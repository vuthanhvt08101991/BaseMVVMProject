package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.Scroller
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.utils.convertDpToPx
import com.google.android.material.tabs.TabLayout

/**
 *  Create by thanhva on 19/08/2020
 *  Class CustomViewPager
 */
open class CustomViewPager : ViewPager {

    private var mScroller: ScrollerCustomDuration? = null

    private var block = true
    private var currentPosition = 0
    private var checkScrollNext = true
    private var saveState = 0

    var swipeEnabled: Boolean = true

    private val MARGIN_START_END = 16f
    private val MARGIN_BETWEEN = 10f


    constructor(context: Context) : super(context) {
        postInitViewPager()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        postInitViewPager()
    }

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private fun postInitViewPager() {
        try {
            val scroller = ViewPager::class.java.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller = ScrollerCustomDuration(
                context,
                interpolator.get(null) as Interpolator
            )
            scroller.set(this, mScroller)
        } catch (e: Exception) {
        }

    }

    /**
     * Set the factor by which the duration will change
     */
    fun setScrollDurationFactor(scrollFactor: Double) {
        mScroller?.setScrollDurationFactor(scrollFactor)
    }

    fun setTab(tabLayout: TabLayout) {

        tabLayout.setupWithViewPager(this)
        tabLayout.addOnTabSelectedListener(object :
            TabLayout.ViewPagerOnTabSelectedListener(this) {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setCurrentItem(tab.position, false)
            }
        })
        val tabCount = tabLayout.tabCount
        for (i in 0 until tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            when (i) {
                0 -> {
                    p.setMargins(
                        convertDpToPx(context, MARGIN_START_END),
                        0,
                        convertDpToPx(context, MARGIN_BETWEEN),
                        0
                    )
                }

                tabCount - 1 -> {
                    p.setMargins(0, 0, convertDpToPx(context, MARGIN_START_END), 0)
                }

                else -> {
                    p.setMargins(0, 0, convertDpToPx(context, MARGIN_BETWEEN), 0)
                }
            }
            try {
                ((tab as ViewGroup).getChildAt(1) as TextView).includeFontPadding = false
            } catch (ex: Exception) {
                // do nothing
            }
            tab.requestLayout()
        }

        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                /* 1: Scroll
                   2: selected
                   0: scorll done */
                saveState = state
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Control when swipe right to left
                if (checkScrollNext) {
                    if (currentPosition == position) {
                        block = false
                    }
                    if (!block) {
                        if (position > 0) {
                            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position - 1)
                                .background = null
                        }
                        //Set background current tab
                        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position).apply {
                            background =
                                resources.getDrawable(R.drawable.bg_tab_top, context.theme)
                            background.alpha = (255 * (1 - positionOffset)).toInt()
                        }
                        if (position < tabLayout.tabCount - 1) {
                            //Set background next tab
                            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position + 1)
                                .apply {
                                    background = resources.getDrawable(
                                        R.drawable.bg_tab_top,
                                        context.theme
                                    )
                                    background.alpha = (255 * (positionOffset)).toInt()
                                }
                        }
                        //Set background next 2 tab
                        if (position == currentPosition - 1 && position
                            < tabLayout.tabCount.minus(2)
                        ) {
                            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position + 2)
                                .background = null
                        }
                    }
                }
                // Control when swipe left to right
                if (!checkScrollNext) {
                    if (currentPosition - 1 == position) {
                        block = false
                    }
                    if (!block) {
                        //Set background current tab
                        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position).apply {
                            background =
                                resources.getDrawable(R.drawable.bg_tab_top, context.theme)
                            background.alpha = (255 * (1 - positionOffset)).toInt()
                        }
                        //Set background next tab
                        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position + 1)
                            .apply {
                                background =
                                    resources.getDrawable(R.drawable.bg_tab_top, context.theme)
                                background.alpha = (255 * (positionOffset)).toInt()
                            }
                        //Set background next 2 tab
                        if (position < tabLayout.tabCount.minus(2)) {
                            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position + 2)
                                .background = null
                        }
                        //Set background preview tab
                        if (currentPosition == position && position > 0) {
                            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position - 1)
                                .background = null
                        }
                        return
                    }
                    //handle when swipe back and nexxt when state = selected
                    if (currentPosition == position && saveState == STATE_SELECTED && position
                        < tabLayout.tabCount.minus(1)
                    ) {

                        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position + 1)
                            .apply {
                                background =
                                    resources.getDrawable(
                                        R.drawable.bg_tab_top,
                                        context?.theme
                                    )
                                background.alpha = (255 * (positionOffset)).toInt()
                            }
                    }
                    //handle  when state = scroll done
                    if (saveState == STATE_SCROLL_DONE) {

                        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position + 1)
                            .background = null
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                block = true
                checkScrollNext = currentPosition <= position
                currentPosition = position

                for (i in 0 until tabLayout.tabCount) {
                    if (i == position) continue
                    val tab1 = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
                    if (tab1.background != null)
                        tab1.background = null
                }
            }
        })

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Check allow swiping to switch between pages
        if (this.swipeEnabled) {
            try {
                return super.onInterceptTouchEvent(event)
            } catch (e: IllegalArgumentException) {
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Check allow swiping to switch between pages
        if (this.swipeEnabled) {
            try {
                return super.onTouchEvent(event)
            } catch (ex: IllegalArgumentException) {
            }
        }
        return false
    }

    companion object {
        private const val STATE_SCROLL = 0
        private const val STATE_SELECTED = 1
        private const val STATE_SCROLL_DONE = 2
    }

    inner class ScrollerCustomDuration : Scroller {

        private var mScrollFactor = 1.0

        constructor(context: Context) : super(context)

        constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

        @SuppressLint("NewApi")
        constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(
            context,
            interpolator,
            flywheel
        )

        /**
         * Set the factor by which the duration will change
         */
        fun setScrollDurationFactor(scrollFactor: Double) {
            mScrollFactor = scrollFactor
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, (duration * mScrollFactor).toInt())
        }

    }
}