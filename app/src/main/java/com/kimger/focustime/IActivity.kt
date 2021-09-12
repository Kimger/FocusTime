package com.kimger.focustime

import android.os.Bundle
import androidx.annotation.LayoutRes

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/22 10:05
 * @description
 */
interface IActivity {

    fun beforeCreate()

    @LayoutRes
    fun getLayoutId(): Int

    fun init()

    fun initEvent()

    fun initData()

    fun onCreated(saveInstanceState: Bundle?)

    fun onInitialized(saveInstanceState: Bundle?)

    fun startActivity(cls: Class<*>)

    fun startActivityForResult(cls: Class<*>, requestCode: Int)

    fun startActivity(key: String, value: String, cls: Class<*>)

}
