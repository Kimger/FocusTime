package com.kimger.focustime

import com.orhanobut.hawk.Hawk

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 18:57
 * @email kimger@cloocle.com
 * @description
 */
class Cache {
    companion object {
        fun saveCache(key: String, value: Any) {
            Hawk.put(key, value)
        }

        fun <T> get(key: String): T {
            return Hawk.get(key) as T
        }
    }
}