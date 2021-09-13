package com.kimger.focustime

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Process
import java.util.*
import kotlin.system.exitProcess

/**
 * @author Kimger
 * @email kimger@cloocle.com
 * @date 2019/5/23 11:10
 * @description
 */
class AppManager private constructor() {

    //  activity管理栈
    private val activityStack: Stack<Activity> = Stack()

    companion object {
        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { AppManager() }
    }

    /**
     * 入栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 出栈
     */
    fun finishActivity(activity: Activity) {
        activity.finish()
        activityStack.remove(activity)
    }

    /**
     * 当前activity,也就是栈的最后一位元素
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    fun getActivity(activity: String): Activity? {
        for (aty in activityStack) {
            if (aty.javaClass.simpleName == activity) {
                return aty
            }
        }
        return null
    }

    /**
     * 清理栈
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

    /**
     * 退出应用
     */
    @SuppressLint("MissingPermission")
    fun exitApplication(context: Context) {
        finishAllActivity()
        val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        exitProcess(0)
    }

    fun restartApp(context: Activity) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
        Process.killProcess(Process.myPid())
    }
}