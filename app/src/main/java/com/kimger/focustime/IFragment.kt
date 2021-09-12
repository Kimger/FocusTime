package com.kimger.focustime

import androidx.annotation.LayoutRes

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/22 14:34
 * @description
 */
interface IFragment {
    @LayoutRes
    fun getLayoutId(): Int

    fun init()

    fun initEvent()

    fun initData()
    
    fun loadData()

    fun beferCreated()

    fun onInitialized()

    fun startActivity(cls: Class<*>)

    fun startActivity(key: String, value: String, cls: Class<*>)

    fun setStatusBar(visible: Boolean)

    fun lazyLoad()
}