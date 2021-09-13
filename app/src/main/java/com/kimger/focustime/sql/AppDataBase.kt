package com.kimger.focustime.sql

import androidx.room.*
import com.kimger.focustime.sql.entity.TodoListEntity
import com.kimger.focustime.sql.entity.TodoRecordEntity

@Database(entities = [TodoListEntity::class,TodoRecordEntity::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
    abstract fun todoRecordDao(): TodoRecordDao
}