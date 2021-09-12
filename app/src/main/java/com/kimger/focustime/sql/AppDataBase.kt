package com.kimger.focustime.sql

import androidx.room.*
import com.kimger.focustime.sql.entity.TodoListEntity

@Database(entities = [TodoListEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

}