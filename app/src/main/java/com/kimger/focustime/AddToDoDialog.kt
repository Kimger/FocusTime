package com.kimger.focustime

import android.view.View
import com.kimger.focustime.sql.DatabaseManager
import com.kimger.focustime.sql.entity.TodoListEntity
import kotlinx.android.synthetic.main.dialog_add_todo.*

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 17:46
 * @email kimger@cloocle.com
 * @description
 */
class AddToDoDialog : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_add_todo

    override fun init(view: View) {
        tv_commit.setOnClickListener {
            val title = et_input.text.trim().toString()
            val time = et_input_time.text.trim().toString()
            val todoBean = TodoListEntity(title, time.toLong() * 60)
            DatabaseManager.dataBase.todoDao().insertTodo(todoBean)
            dismiss()
        }
    }

}