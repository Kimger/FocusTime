package com.kimger.focustime

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-13 11:10
 * @email kimger@cloocle.com
 * @description
 */
data class OneTipBean(
    val commit_from: String = "",
    val created_at: String = "",
    val creator: String = "",
    val creator_uid: Int = 0,
    val from: String = "",
    val from_who: Any = Any(),
    val hitokoto: String = "",
    val id: Int = 0,
    val length: Int = 0,
    val reviewer: Int = 0,
    val type: String = "",
    val uuid: String = ""
)