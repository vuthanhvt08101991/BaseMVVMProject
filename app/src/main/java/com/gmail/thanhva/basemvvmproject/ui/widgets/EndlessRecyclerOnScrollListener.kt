package com.gmail.thanhva.basemvvmproject.ui.widgets

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  Create by thanhva on 05/12/2019
 *  Class EndlessRecyclerOnScrollListener
 */
abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {
    // The total number of items in the dataset after the last load
    var mControlsVisible = true
    private var mPreviousTotal: Int = 0
    private var isLoading = true
    private var mFirstVisibleItem: Int = 0
    private var mVisibleItemCount: Int = 0
    private var mTotalItemCount: Int = 0
    private var mNumberThreshold: Int = 5
    private var mScrolledDistance = 0
    private var state = 0

    override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recycler, dx, dy)
        if (state != 1) {
            val firstVisibleItem =
                (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (firstVisibleItem == 0) {
                if (!mControlsVisible) {
                    mControlsVisible = true
                }
            } else {
                if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                    mControlsVisible = false
                    mScrolledDistance = 0
                } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                    mControlsVisible = true
                    mScrolledDistance = 0
                }
            }
            if (mControlsVisible && dy > 0 || !mControlsVisible && dy < 0) {
                mScrolledDistance += dy
            }
        }

        mVisibleItemCount = recycler.childCount
        mTotalItemCount = recycler.layoutManager?.itemCount ?: 0
        mFirstVisibleItem = when (recycler.layoutManager) {
            is LinearLayoutManager -> {
                (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
            is GridLayoutManager -> {
                (recycler.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
            else -> {
                throw RuntimeException("Un support this kind of LayoutManager ")
            }
        }

        if (isLoading) {
            stateLoading()
        }

        if (!isLoading && mTotalItemCount - mVisibleItemCount <= mFirstVisibleItem + mNumberThreshold && dy > 0) {
            // End has been reached
            onLoadMore()
            isLoading = true
        }
    }

    private fun stateLoading() {
        if (mTotalItemCount != mPreviousTotal) {
            isLoading = false
            mPreviousTotal = mTotalItemCount
        }
    }

    fun resetOnLoadMore() {
        mFirstVisibleItem = 0
        mVisibleItemCount = 0
        mTotalItemCount = 0
        mPreviousTotal = 0
        isLoading = true
    }

    abstract fun onLoadMore()

    companion object {
        private const val HIDE_THRESHOLD = 20
    }
}
