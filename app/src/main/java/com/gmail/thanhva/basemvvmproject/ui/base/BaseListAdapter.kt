package com.gmail.thanhva.basemvvmproject.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gmail.thanhva.basemvvmproject.listener.BaseRecyclerAdapterCallBack
import java.util.concurrent.Executors

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseListAdapter
 */
abstract class BaseListAdapter<Item, VH : BaseViewHolder<Item>>(
    callBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, BaseViewHolder<Item>>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    private val baseRecyclerAdapterCallBacks = ArrayList<BaseRecyclerAdapterCallBack>()
    private var listData: ArrayList<Item>? = null

    protected abstract fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseViewHolder<Item>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewHolder(parent, LayoutInflater.from(parent.context), viewType)

    override fun onBindViewHolder(holder: BaseViewHolder<Item>, position: Int) {
        holder.onBindData(getItem(position))
        holder.binding.executePendingBindings()
    }

    override fun submitList(list: List<Item>?) {
        for (callback in baseRecyclerAdapterCallBacks) {
            callback.itemCount(list?.size ?: 0)
        }
        super.submitList(ArrayList<Item>(list ?: listOf()))
    }

    fun addBaseRecyclerAdapterCallBack(baseRecyclerAdapterCallBack: BaseRecyclerAdapterCallBack) {
        baseRecyclerAdapterCallBacks.add(baseRecyclerAdapterCallBack)
    }

    fun getData(): ArrayList<Item> {
        return listData ?: ArrayList<Item>().apply { listData = this }
    }

    fun setData(data: List<Item>?) {
        getData().clear()
        data?.let {
            getData().addAll(it)
        }
        submitList(getData())
    }

    fun addData(data: List<Item>?) {
        getData().addAll(data ?: ArrayList())
        submitList(ArrayList<Item>(getData()))
    }

    fun addData(data: Item?) {
        data?.let { getData().add(data) }
        submitList(getData())
    }

    fun addData(data: Item?, position: Int) {
        data?.let { getData().add(position, data) }
        submitList(getData())
    }

    fun clearData() {
        getData().clear()
        submitList(getData())
    }

    fun refreshData() {
        submitList(getData())
    }
}