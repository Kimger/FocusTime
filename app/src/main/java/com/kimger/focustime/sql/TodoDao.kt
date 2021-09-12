package com.kimger.focustime.sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kimger.focustime.sql.entity.TodoListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("select * from todoList")
    fun getTodoList(): Flow<List<TodoListEntity>>

    @Insert
    fun insertTodo(entity: TodoListEntity)

    @Delete
    fun deleteTodo(entity: TodoListEntity)
}