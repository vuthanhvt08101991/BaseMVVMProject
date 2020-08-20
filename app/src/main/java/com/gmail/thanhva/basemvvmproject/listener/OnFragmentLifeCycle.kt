package com.gmail.thanhva.basemvvmproject.listener

/**
 *  Create by thanhva on 18/08/2020
 *  Class OnFragmentLifeCycle
 */
interface OnFragmentLifeCycle {

    fun onFragmentDestroy(tag: String)

    /**
     * Call back invoke when screen need force update
     * @param isUpdate true if screen need update
     */
    fun onForceUpdate(tag: String, isUpdate: Boolean) {}
}