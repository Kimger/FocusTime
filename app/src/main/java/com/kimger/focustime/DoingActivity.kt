package com.kimger.focustime

import com.kimger.focustime.sql.entity.TodoListEntity
import kotlinx.android.synthetic.main.activity_doing.*

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 16:03
 * @email kimger@cloocle.com
 * @description
 */
class DoingActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_doing

    override fun init() {

        val todoBean: TodoListEntity = intent.getSerializableExtra("data") as TodoListEntity
        doingView.setTodoData(todoBean)
        doingView.setOnStopListener { finish() }

    }

    override fun initEvent() {
        super.initEvent()
    }

    override fun allowBackKeyDown(): Boolean {
        return false
    }

}