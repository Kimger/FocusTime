package com.kimger.focustime.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kimger.focustime.Helper
import java.io.Serializable

@Entity(tableName = "todoList")
data class TodoListEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var time: Long = 0L,
    var backgroundId: Int = Helper.get().getRandomBackground()
) : Serializable {
    constructor(title: String, time: Long) : this() {
        this.title = title
        this.time = time
    }
}