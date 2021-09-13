package com.kimger.focustime.sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kimger.focustime.sql.entity.TodoRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-13 14:05
 * @email kimger@cloocle.com
 * @description
 */
@Dao
interface TodoRecordDao {

    @Query("select * from todoRecord")
    fun getTodoRecord(): Flow<List<TodoRecordEntity>>

    @Insert
    fun insert(entity: TodoRecordEntity)

    @Delete
    fun delete(entity: TodoRecordEntity)
}