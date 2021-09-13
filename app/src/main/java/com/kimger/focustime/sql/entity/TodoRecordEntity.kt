package com.kimger.focustime.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kimger.focustime.Helper
import java.io.Serializable
import java.util.*

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-13 14:06
 * @email kimger@cloocle.com
 * @description
 */
@Entity(tableName = "todoRecord")
data class TodoRecordEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var createDate: Long = Date().time,
    var status: Int = 0,//1 完成，2 放弃
    var todoId: Int = 0,
    var title: String = "",
    var time: Long = 0L,
    var backgroundId: Int = 0,
    var userTime: Long = 0L
) : Serializable {
    constructor(
        status: Int,
        todoId: Int = 0,
        title: String = "",
        time: Long = 0L,
        backgroundId: Int = 0,
        userTime: Long = 0,
    ) : this() {
        this.status = status
        this.todoId = todoId
        this.title = title
        this.time = time
        this.backgroundId = backgroundId
        this.userTime = userTime
    }
}
