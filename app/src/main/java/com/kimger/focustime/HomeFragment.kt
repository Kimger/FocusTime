package com.kimger.focustime

import android.content.Intent
import com.kimger.focustime.adpater.AbstractAdapter
import com.kimger.focustime.adpater.setUp
import com.kimger.focustime.sql.DatabaseManager
import com.kimger.focustime.sql.entity.TodoListEntity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_todo_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 15:22
 * @email kimger@cloocle.com
 * @description
 */
class HomeFragment : BaseFragment() {

    private lateinit var adapter: AbstractAdapter<TodoListEntity>
    private lateinit var todoList: MutableList<TodoListEntity>

    override fun getLayoutId() = R.layout.fragment_home


    override fun init() {
        todoList = ArrayList()

        adapter = rv_todo_list.setUp(todoList, R.layout.item_todo_list, { holder, todoBean ->
            holder.itemView.cl_root.setBackgroundResource(todoBean.backgroundId)
            holder.itemView.tv_item_title.text = todoBean.title
            holder.itemView.tv_item_time.text = "${Helper.get().time2m(todoBean.time)}分钟"
            holder.itemView.tv_item_start.setOnClickListener { start(todoBean) }
            holder.itemView.delete.setOnClickListener {
                launch(Dispatchers.IO) {
                    DatabaseManager.dataBase.todoDao().deleteTodo(todoBean)
                }
            }

        })
        rv_todo_list.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(mActivity))

        launch(Dispatchers.IO) {
            DatabaseManager.dataBase.todoDao().getTodoList().collect {
                withContext(Dispatchers.Main) {
                    adapter.update(it.toMutableList())
                }
            }
        }


//        adapter.update(todoList)
    }

    override fun initEvent() {
        super.initEvent()
        iv_add.setOnClickListener {
            val addToDoDialog = AddToDoDialog()
            addToDoDialog.show(childFragmentManager)
        }
    }

    private fun start(data: TodoListEntity) {
        val lockMode = Hawk.get("lockMode", 0)
        if (lockMode == 0) {
            val intent = Intent(mActivity, DoingActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        } else {
            EasyFloat.with(mActivity)
                .setLayout(R.layout.activity_doing) {
                    val doingView = it.findViewById<DoingView>(R.id.doingView)
                    doingView.setTodoData(data)
                    doingView.setOnStopListener { EasyFloat.dismiss() }
                }
                .setImmersionStatusBar(true)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setMatchParent(widthMatch = true, heightMatch = true)
                .show()
        }


    }

}