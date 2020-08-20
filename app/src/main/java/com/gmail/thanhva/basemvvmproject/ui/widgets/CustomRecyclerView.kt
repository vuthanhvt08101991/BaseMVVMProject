package com.gmail.thanhva.basemvvmproject.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gmail.thanhva.basemvvmproject.databinding.CustomRecyclerViewBinding
import com.gmail.thanhva.basemvvmproject.listener.BaseRecyclerAdapterCallBack
import com.gmail.thanhva.basemvvmproject.ui.base.BaseListAdapter
import com.gmail.thanhva.basemvvmproject.utils.setUpRecyclerViewVertical


@BindingMethods(
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "load_more",
        method = "setLoadMore"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "isRefresh",
        method = "setRefresh"
    )
)
class CustomRecyclerView : LinearLayout {
    var adapterCustomRecyclerView: BaseListAdapter<*, *>? = null
    var isAppBarShow: Boolean = false
    var customRecyclerViewCallback: CustomRecyclerViewCallback? = null
    private var emptyView: View? = null
    private var positonTouch = 0F
    private lateinit var customRecyclerViewBinding: CustomRecyclerViewBinding

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        val positionY = event!!.y

        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            positonTouch = positionY

        }

        if (event.actionMasked == MotionEvent.ACTION_UP) {
            if (isAppBarShow) {
                customRecyclerViewBinding.refresh.isEnabled = true
            }
        }

        if (event.actionMasked == MotionEvent.ACTION_MOVE) {
            if (positionY > positonTouch && !isAppBarShow) {
                customRecyclerViewBinding.refresh.isEnabled = false
                isAppBarShow = true
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun initView() {
        customRecyclerViewBinding =
            CustomRecyclerViewBinding.inflate(LayoutInflater.from(context), this, true)

        customRecyclerViewBinding.recyclerView.apply {
            setUpRecyclerViewVertical()
            addOnScrollListener(endlessRecyclerOnScrollListener)
        }
        customRecyclerViewBinding.refresh.setOnRefreshListener { refreshData() }
    }

    private val endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() {
            customRecyclerViewCallback?.onLoadMore()
        }
    }

    private fun refreshData() {
        endlessRecyclerOnScrollListener.resetOnLoadMore()
        customRecyclerViewCallback?.onRefresh()
    }

    fun getRecyclerView(): RecyclerView = customRecyclerViewBinding.recyclerView

    fun getSwipeRefreshLayout(): SwipeRefreshLayout = customRecyclerViewBinding.refresh

    fun setLoadMore(isLoadMore: Boolean = false) {
        customRecyclerViewBinding.progressLoadMore.visibility =
            if (isLoadMore) View.VISIBLE else View.GONE
    }

    fun setRefresh(isRefresh: Boolean) {
        if (isRefresh) {
            endlessRecyclerOnScrollListener.resetOnLoadMore()
        }
        customRecyclerViewBinding.refresh.isRefreshing = isRefresh
    }

    fun addEmptyView(view: View) {
        emptyView = view
        customRecyclerViewBinding.layoutNoData.visibility = View.GONE
        customRecyclerViewBinding.layoutNoData.addView(
            emptyView,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
    }

    fun setRecyclerAdapter(recyclerAdapter: BaseListAdapter<*, *>) {
        if (adapterCustomRecyclerView == null) {
            adapterCustomRecyclerView = recyclerAdapter
            customRecyclerViewBinding.recyclerView.adapter = adapterCustomRecyclerView
            adapterCustomRecyclerView?.addBaseRecyclerAdapterCallBack(object :
                BaseRecyclerAdapterCallBack {
                override fun itemCount(count: Int) {
                    customRecyclerViewBinding.layoutNoData.visibility =
                        if (count <= 0) View.VISIBLE else View.GONE
                }
            })
        }
    }

    fun scrollToPosition(position: Int) {
        customRecyclerViewBinding.recyclerView.smoothScrollToPosition(position)
    }

    fun clearData() {
        endlessRecyclerOnScrollListener.resetOnLoadMore()
    }

    fun setControlsVisible(){
        endlessRecyclerOnScrollListener.mControlsVisible = true
    }
}

interface CustomRecyclerViewCallback {
    fun onLoadMore()
    fun onRefresh()
}
