package com.kimger.focustime

import android.app.Application
import com.orhanobut.hawk.Hawk

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 14:52
 * @email kimger@cloocle.com
 * @description
 */
class App : Application() {
    companion object{
        lateinit var instance : App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(this).build()
    }


}