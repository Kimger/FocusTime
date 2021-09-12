package com.kimger.focustime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 15:39
 * @email kimger@cloocle.com
 * @description
 */
infix fun ViewGroup.inflate(layoutResId: Int): View =
    LayoutInflater.from(context).inflate(layoutResId, this, false)

fun String?.isNotNull() = !isNullOrEmpty()

fun String?.isNull() = isNullOrBlank()

fun Any.toJson(): String = Gson().toJson(this)

fun <T> String.toObject(clazz: Class<T>): T = Gson().fromJson<T>(this, clazz)