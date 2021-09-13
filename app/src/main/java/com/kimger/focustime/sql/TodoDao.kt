package com.kimger.focustime.sql

import androidx.room.*
import com.kimger.focustime.sql.entity.TodoListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("select * from todoList")
    fun getTodoList(): Flow<List<TodoListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(entity: TodoListEntity)

    @Delete
    fun deleteTodo(entity: TodoListEntity)

    @Update
    fun updateTodo(entity: TodoListEntity)
}