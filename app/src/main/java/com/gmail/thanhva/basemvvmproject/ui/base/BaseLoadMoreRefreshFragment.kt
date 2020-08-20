package com.gmail.thanhva.basemvvmproject.ui.base

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.ui.widgets.CustomRecyclerView
import com.gmail.thanhva.basemvvmproject.ui.widgets.CustomRecyclerViewCallback

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseLoadMoreRefreshFragment
 */
abstract class BaseLoadMoreRefreshFragment<View : ViewDataBinding, ViewModel : BaseLoadMoreRefreshViewModel<Item>, Item> :
    BaseFragmentBinding<View, ViewModel>() {

    abstract val customRecyclerView: CustomRecyclerView

    abstract val adapter: BaseListAdapter<Item, out BaseViewHolder<Item>>

    @CallSuper
    override fun initView() {
        customRecyclerView.customRecyclerViewCallback = object : CustomRecyclerViewCallback {
            override fun onLoadMore() {
                viewModel.loadMore()
            }

            override fun onRefresh() {
                viewModel.refreshData()
            }
        }
    }

    @CallSuper
    override fun observeField() {
        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            customRecyclerView.setRefresh(it)
        })

        viewModel.listItem.observe(viewLifecycleOwner, Observer { items ->

            if (customRecyclerView.adapterCustomRecyclerView == null) {
                customRecyclerView.setRecyclerAdapter(adapter)
            }

            if (viewModel.page == viewModel.getDefaultFirstPage()) {
                setDataRecyclerView(items)
            } else {
                addDataRecyclerView(items)
            }
        })

        viewModel.isLoadMoreFail.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                customRecyclerView.clearData()
            }
        })
    }

    open fun setDataRecyclerView(list: MutableList<Item>){
        adapter.setData(list)
    }

    open fun addDataRecyclerView(list: MutableList<Item>){
        adapter.addData(list)
    }

    fun clearData(){
        customRecyclerView.clearData()
        adapter.clearData()
    }
}